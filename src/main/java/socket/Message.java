package socket;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author chumeng
 * @date 2022/10/4
 */
public class Message implements Serializable {
    String name;
    LocalDateTime time;
    String message;

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
        time = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return name + " (" + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")" + " : " + message;
    }
}
