package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * @author chumeng
 * date 2022/10/4
 */
public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(HOST, PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(),true, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 登入和注册
        System.out.println("请选择：登入（1）｜ 注册（2）");
        Scanner scanner = new Scanner(System.in);
        String flag = scanner.nextLine();
        if (Objects.equals(flag, "1")) {
            writer.println("1");
            System.out.println("请输入账户：");
            writer.println(scanner.nextLine());
            System.out.println("请输入密码：");
            writer.println(scanner.nextLine());
            if (Objects.equals(reader.readLine(), "登入失败")){
                error("登入失败，程序即将退出");
                return;
            } else {
                System.out.println("登入成功！");
            }
        } else if (Objects.equals(flag, "2")){
            writer.println("2");
            System.out.println("请输入账户：");
            writer.println(scanner.nextLine());
            System.out.println("请输入密码：");
            writer.println(scanner.nextLine());
            System.out.println("请输入用户名：");
            writer.println(scanner.nextLine());

            if (Objects.equals(reader.readLine(), "注册失败")){
                error("注册失败，程序即将退出");
                return;
            } else {
                error("注册成功，自动登入中");
            }
        } else {
            error("未知选项，程序即将退出");
            return;
        }

        // 聊天室
        System.out.println("请选择：进入聊天室（1）｜ 创建聊天室（2）｜ 退出（3）");
        flag = scanner.nextLine();
        while (true) {
            if (Objects.equals(flag, "1")) {
                writer.println("1");
                System.out.println("请输入id：");
                writer.println(scanner.nextLine());
                String message = reader.readLine();
                System.out.println(message);
                if (!"已进入聊天室".equals(message)) {
                    error("聊天室 id 错误，程序即将退出！");
                    return;
                }
                break;
            } else if (Objects.equals(flag, "2")) {
                writer.println("2");
                String uuid = UUID.randomUUID().toString();
                System.out.println("聊天室已创建，id为：" + uuid);
                System.out.println("已进入聊天室");
                writer.println(uuid);
                break;
            } else if (Objects.equals(flag, "3")) {
                writer.println("3");
                error("准备退出！");
                return;
            }else{
                System.out.println("未知选择，请重新选择！");
            }
            System.out.println("请选择：进入聊天室（1）｜ 创建聊天室（2）｜ 退出（3）");
        }


        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String message;
                while (((message = reader.readLine()) != null) && (!"null".equals(message))) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        while(true){
            writer.println(scanner.nextLine());
        }
    }

    public static void error(String message) throws InterruptedException {
        System.out.println(message);
        for (int i = 0; i < 10; i++) {
            System.out.print("-");
            Thread.sleep(200);
        }
    }
}
