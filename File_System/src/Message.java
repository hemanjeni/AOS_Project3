import java.io.Serializable;

public class Message  implements Serializable, Comparable<Message>{

    private static final long serialVersionUID = 1L;
    private int senderID;
    private int offset;
    private int index;
    private String fileName;
    private MessageType msgtype;

    public Message(int senderUID, MessageType Msgtype, String FileName, int index, int offset ) {
        this.senderID = senderUID;
        this.msgtype = Msgtype;
        this.fileName = FileName;
        this.index = index;
        this.offset = offset;
    }

    public Message(int senderUID, MessageType Msgtype, String FileName, int index) {
        this.senderID = senderUID;
        this.msgtype = Msgtype;
        this.fileName = FileName;
        this.index = index;
    }
    public Message(int senderUID, MessageType Msgtype, String FileName) {
        this.senderID = senderUID;
        this.msgtype = Msgtype;
        this.fileName = FileName;

    }
    @Override
    public int compareTo(Message o) {
        return 0;
    }
}
