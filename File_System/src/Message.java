import java.io.Serializable;

public class Message  implements Serializable{

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

    public int getSenderID() {
        return senderID;
    }

    public int getOffset() {
        return offset;
    }

    public int getIndex() {
        return index;
    }

    public String getFileName() {
        return fileName;
    }

    public MessageType getMsgtype() {
        return msgtype;
    }
}
