//package testing;
import java.io.*;
        import java.net.*;
        import java.util.*;
        import java.util.logging.FileHandler;
        import java.util.logging.Logger;
        import java.util.logging.SimpleFormatter;
        import java.util.concurrent.BlockingQueue;
        import java.util.concurrent.PriorityBlockingQueue;

public class MetadataServer {

    static DataOutputStream dos[] = new DataOutputStream[7];
    static DataInputStream dis[] = new DataInputStream[7];
    static ServerSocket ss[] = new ServerSocket[7];
    static Socket s[] = new Socket[7];
    int file_server_no = 0;
    static Thread[] t;
    static int completioncounter = 0;
    static boolean stopflag = false;
    Logger logger;
    BlockingQueue<Message> msgQueue;

    private Map<String, List<String>> linuxfileToChunks;
    private Map<String,  List<String>> chunkToReplicas;
    private Map<String,  Integer> replicaToServer;
    private Map<String, String> replicaToChunk;
    private Map<String, Integer> chunkVersion;
    private Map<String, Integer> replicaVersion;
    private Map<String, Long> replicaSize;


    private Map<Integer, Boolean> serversConnected;
    private Map<Integer, Long> lastHeartbeat;


    public MetadataServer(String args[]) throws IOException {
        msgQueue = new PriorityBlockingQueue<Message>();
        linuxfileToChunks = new HashMap<>();
        replicaToServer = new HashMap<>();
        chunkVersion = new HashMap<>();
        chunkToReplicas = new HashMap<>();
        replicaToChunk = new HashMap<>();
        replicaVersion = new HashMap<>();
        replicaSize = new HashMap<>();

        serversConnected = new HashMap<>();
        lastHeartbeat = new HashMap<>();




        ConfigProperties prop = new ConfigProperties();
        int metadataServeraddressforserver1 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver1"));

        try {
            //logger for file server
            File logDir = new File("./logs/");
            if (!(logDir.exists()))
                logDir.mkdir();
            logger = Logger.getLogger("MetadataServer.log");

            FileHandler fh = new FileHandler("logs/MetadataServer.log");
            fh.setFormatter(new SimpleFormatter());
            // fh.setLevel(logLevel);
            logger.addHandler(fh);
            logger.info("MetadataServer  initialization");


            for (int i = 0; i < 7; i++)
            {
                ss[i] = new ServerSocket(metadataServeraddressforserver1 + i);
                s[i] = ss[i].accept();
                dos[i] = new DataOutputStream(s[i].getOutputStream());
                dis[i] = new DataInputStream(s[i].getInputStream());

                Thread t = new Thread(new ChannelHandler(s[i]));
                t.start();
                System.out.print("Starting thread number" + i);
                logger.info("Starting thread number" + i);
            }
            // accept connections from clients
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //reading from queue for new messages
        while(true){

            for(int serverID : serversConnected.keySet()){
                if (lastHeartbeat.containsKey(serverID) &&
                        (System.currentTimeMillis() / 1000) - lastHeartbeat.get(serverID) >= 15) {
                    serversConnected.put(serverID, false);
                }

            }

            if(msgQueue.size() !=0){
                Message msg = msgQueue.peek();
                msgQueue.remove();
                Random rand = new Random();
                //temp hasmap will be replaced with servers connected will store NodeID and true false
                Map<Integer,Boolean> temp = new HashMap<>();


                if(msg.getMsgtype() == MessageType.Create){
                    // 3 server selected to create a linux file with with chunk 1
                    List<Integer> serversSelected = randomServers();
                    String linuxFilename = msg.getFileName();
                    String chunkName =  linuxFilename + rand.nextInt(100);


                    //below code will be used for future purpose.

					for(int i : serversSelected) {
						if (temp.get(i)) {
						    /*it will call the function to send message to create chunk to the selected server where i
						    is ID of server
                            */
						    if(serversConnected.get(i)) {
                                String replicaName = chunkName + i + rand.nextInt(10);
                                sendMessageToServer(i, MessageType.Create, chunkName, linuxFilename, replicaName,
                                        null, (long) 0);
                            }
						    else{
                                System.out.println("server is dead, ID:" + i);
                            }
						}

					}

                }

                if(msg.getMsgtype() == MessageType.Append){
                    String linuxFileName = msg.getFileName();
                    String dataForAppend = msg.getData();
                    long appendDateSize = dataForAppend.getBytes().length;
                    //check with KD
                    List<String> chunksNames = linuxfileToChunks.get(linuxFileName);
                    String lastChunk = chunksNames.get(chunksNames.size()-1);
                    List<String> replicasName = chunkToReplicas.get(lastChunk);

                        for(String replica : replicasName){
                            Long currentReplicaSize = replicaSize.get(replica);
                            if(4096 - currentReplicaSize > appendDateSize){
                                int serverID = replicaToServer.get(replica);
                                if(serversConnected.get(serverID)) {
                                    sendMessageToServer(serverID, MessageType.Append, lastChunk, linuxFileName, replica,
                                            dataForAppend, currentReplicaSize + appendDateSize);

                                    //update metaData here
                                    replicaSize.put(replica, currentReplicaSize + appendDateSize);
                                /* todo update offset values here! need hasmap with values that has starting offset and
                                ending value of offset */
                                }
                            }

                            else{
                                // to create file 2 phase commite this will be an issue
                            }

                        }

                }


                else if(msg.getMsgtype() == MessageType.Read){
                    String linuxFileName = msg.getFileName();

                    //check with KD
                    List<String> chunkNames = linuxfileToChunks.get(linuxFileName);
                    int offset = msg.getOffset();





                }
                else if(msg.getMsgtype() == MessageType.Heartbeat){
                    int serverID = msg.getSenderID();
                    serversConnected.put(serverID, true);
                    lastHeartbeat.put(serverID,System.currentTimeMillis() / 1000);
                    //todo data update as per sending


                }

            }
        }


    }



    class ChannelHandler implements Runnable {
        DataInputStream datainput;
        DataOutputStream dataoutput;
        Socket socket;

        public ChannelHandler(Socket s) {
            try {
                // initializing data i/p and o/p streams
                datainput = new DataInputStream(s.getInputStream());
                dataoutput = new DataOutputStream(s.getOutputStream());
                System.out.print("after socket initialization" + datainput + " " + dataoutput + " " + s);
                logger.info("After socket initialization" + datainput + " " + dataoutput + " " + s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            try {
                System.out.println("Inside run");
                while(true)
                    if (datainput.available() > 0)
                    {
                        logger.info("data available");
                    }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }



    private void sendMessageToServer(){

        //todo
        String addressOfServer;
        int port;


    }

    private void sendMessageToServer(int serverID, MessageType type, String chunkName, String linuxFileName ,
                                     String replicaName, String messageData, Long size){

        //todo use serverId
        String addressOfServer = null; // ip
        int port = 0;//port
        Socket socket = null;
        int counter=0;

        try {
            System.out.println("sending message to server to create file ---- Create/Append file step 1");
            socket = new Socket(addressOfServer, port);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());

            Message message = new Message();
            message.setLinuxFilename(linuxFileName);
            message.setReplicaName(replicaName);
            message.setMsgtype(type);
            message.setData(messageData);

            os.writeObject(message);
                if(counter == 0 && type == MessageType.Create){
                    linuxfileToChunks.putIfAbsent(linuxFileName, new ArrayList<>());
                    linuxfileToChunks.get(linuxFileName).add(chunkName);
                    counter++;
                    updateMetaData(chunkName, serverID, linuxFileName, replicaName , size);
                }

            System.out.println("sending message to server to create file ---- Create/Append file step 2");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void updateMetaData(String chunkName, int serverID ,String linuxFileName , String replicaName , Long dataSize){
        // update the meta data information

        /* already mapped the linuxfile name with chunk at sendMessageToServer, now first mapping the replica to chuck
            then replica to serverID  and then replica version.
        */
        chunkToReplicas.putIfAbsent(chunkName , new ArrayList<>());
        chunkToReplicas.get(chunkName).add(replicaName);
        replicaToServer.putIfAbsent(replicaName, serverID);
        replicaToChunk.putIfAbsent(replicaName, chunkName);
        chunkVersion.put(chunkName,0);
        replicaVersion.putIfAbsent(replicaName,0);
        replicaSize.put(replicaName, dataSize);

    }

    private List<Integer> randomServers() {
        List<Integer> list = new ArrayList<>();

        // add 5 server IDS in ArrayList
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Random randomNumber = new Random();
        int temp = 2;
        int rand;
        while (temp>0){
            rand = randomNumber.nextInt(list.size()+1);
            list.remove(rand);
            temp--;
        }

        return  list;
    }

    /* server number to be given as argument while running */
    public static void main(String[] args) {
        try {
            new MetadataServer(args);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}