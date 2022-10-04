package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chumeng
 * time 2022/10/4
 */
public class Server {
    private static final int PORT = 8000;
    private static HashMap<String,String> userNames = new HashMap<>();
    private static HashMap<String,String> passwords = new HashMap<>();
    private static HashMap<String,Messages> chatRooms = new HashMap<>();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            2,
            20,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static void main(String[] args) throws IOException {
        // 初始用户
        passwords.put("root", "root");
        userNames.put("root", "root");

        ServerSocket serverSocket = new ServerSocket(PORT);
        // 用来记录当前用户名
        String userName = null;
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true, StandardCharsets.UTF_8);

            // 登入验证
            if (Objects.equals(reader.readLine(), "1")) {
                userName = reader.readLine();
                if (!verify(userName,reader.readLine())) {
                    writer.println("登入失败");
                    continue;
                } else {
                    writer.println("登入成功");
                }
            } else {
                userName = reader.readLine();
                if (register(userName,reader.readLine(),reader.readLine())) {
                    writer.println("注册成功");
                } else {
                    writer.println("注册失败");
                    continue;
                }
            }

            // 聊天室
            String str = reader.readLine();
            if (Objects.equals(str, "1")){
                String uuid = reader.readLine();
                System.out.println(uuid);
                System.out.println(chatRooms.size());
                if (chatRooms.containsKey(uuid)){
                    Messages m = chatRooms.get(uuid);
                    writer.println("已进入聊天室");
                    ArrayList<PrintWriter> outs = m.getOuts();
                    outs.add(writer);
                    writer.println("当前聊天室连接数：" + outs.size());
                    writer.println("进行加载信息 ---");
                    ArrayList<Message> messages = m.getMessages();
                    for (Message message : messages) {
                        writer.println(message);
                    }
                    listen(reader,outs,userName,m);
                } else {
                    writer.println("error");
                }
            } else if (Objects.equals(str, "2")){
                String uuid = reader.readLine();
                Messages m = new Messages();
                chatRooms.put(uuid,m);
                System.out.println("已创建聊天室,id:" + uuid + " 当前存在" + chatRooms.size() + "个聊天室");
                ArrayList<PrintWriter> outs = m.getOuts();
                outs.add(writer);
                listen(reader,outs,userName,m);
            }
        }
    }

    /**
     * Returns to verify result
     * @param name
     * @param password
     * @return
     */
    public static boolean verify(String name, String password){
        AtomicBoolean result = new AtomicBoolean(false);
        if (name != null && password != null) {
            passwords.forEach((k,v) -> {
                if (name.equals(k) && password.equals(v)) {
                    result.set(true);
                }
            });
        }
        return result.get();
    }

    /**
     * Returns 注册
     * @param loginName
     * @param password
     * @param userName
     * @return
     */
    public static boolean register(String loginName, String password, String userName) {
        if (loginName != null && password != null && userName != null) {
            passwords.put(loginName, password);
            userNames.put(loginName, userName);
            return true;
        }
        return false;
    }

    /**
     * 客户端连接时创建一个线程
     * Returns
     * @param reader
     * @param outs
     * @throws IOException
     */
    public static void listen(BufferedReader reader,ArrayList<PrintWriter> outs,String userName,Messages m) throws IOException {
        try {
            executor.execute(() -> {
                String name = userNames.get(userName);
                try {
                    while (true) {
                        String str = reader.readLine();
                        Message message = new Message(name,str);
                        m.getMessages().add(message);
                        outs.forEach(out -> out.println(message));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
