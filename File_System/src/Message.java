import java.io.Serializable;

public class Message  implements Serializable, Comparable<Message>{

    private static final long serialVersionUID = 1L;
    private int senderID;
    private int offset;
    private int index;
    private String fileName;
    private MessageType msgtype;
    private String data;

    // for metaDeta
    private String linuxFilename;
    private String replicaName;


    public Message(){}


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

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinuxFilename() {
        return linuxFilename;
    }

    public void setLinuxFilename(String linuxFilename) {
        this.linuxFilename = linuxFilename;
    }

    public MessageType getMsgtype() {
        return msgtype;
    }

    public String getReplicaName() {
        return replicaName;
    }

    public void setReplicaName(String replicaName) {
        this.replicaName = replicaName;
    }

    public void setMsgtype(MessageType msgtype) {
        this.msgtype = msgtype;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int compareTo(Message o) {
        return 0;
    }
}
