package Code;

import java.io.Serializable;
import java.util.Vector;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    public String type, sender, content, recipient;
    Vector<String> rs = new Vector<String>();

    public Message(String type, String sender, String content, String recipient) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
    }

    public Message(String type, String sender, String content, String recipient, Vector<String> rs) {

        this.type = type;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
        this.rs = rs;

    }

    @Override
    public String toString() {
        return "{type='" + type + "', sender='" + sender + "', content='" + content + "', recipient='" + recipient + "'}";
    }

    public Vector<String> getRs() {
        return rs;
    }

    public void setRs(Vector<String> rs) {
        this.rs = rs;
    }

}
