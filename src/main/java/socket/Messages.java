package socket;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author chumeng
 * @date 2022/10/4
 */
public class Messages implements Serializable {
    private ArrayList<Message> messages = new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<PrintWriter> getOuts() {
        return outs;
    }

    public void setOuts(ArrayList<PrintWriter> outs) {
        this.outs = outs;
    }

    private ArrayList<PrintWriter> outs = new ArrayList<>();
}
