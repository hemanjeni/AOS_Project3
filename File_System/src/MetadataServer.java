//package testing;
import javax.sound.midi.Soundbank;
import java.io.*;
        import java.net.*;
        import java.util.*;
        import java.util.logging.FileHandler;
        import java.util.logging.Logger;
        import java.util.logging.SimpleFormatter;
        import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MetadataServer {

    static ObjectOutputStream dos[] = new ObjectOutputStream[7];
    static ObjectInputStream dis[] = new ObjectInputStream[7];
    static ServerSocket ss[] = new ServerSocket[7];
    static Socket s[] = new Socket[7];
    int file_server_no = 0;
    static Thread[] t;
    static int completioncounter = 0;
    static boolean stopflag = false;
    Logger logger;
    BlockingQueue<Object> msgQueue;

    private Map<String, List<String>> linuxfileToChunks;
    private Map<String,  List<String>> chunkToReplicas;
    private Map<String,  Integer> replicaToServer;
    private Map<String, String> replicaToChunk;
    private Map<String, Integer> chunkVersion;
    private Map<String, Integer> replicaVersion;
    private Map<String, Long> replicaSize;
    private Map<String ,Long> chunkSize;
    private Map<String, Integer> replicaOffset;


    private Map<Integer, Boolean> serversConnected;
    private Map<Integer, Long> lastHeartbeat;

    private Map<String, Integer> fileNameMapping;
    static   HashMap<Integer,Socket> connections;


    public MetadataServer(String args[]) throws IOException {
        msgQueue = new LinkedBlockingQueue<Object>();
        linuxfileToChunks = new HashMap<>();
        replicaToServer = new HashMap<>();
        chunkVersion = new HashMap<>();
        chunkToReplicas = new HashMap<>();
        replicaToChunk = new HashMap<>();
        replicaVersion = new HashMap<>();
        replicaSize = new HashMap<>();
        replicaOffset = new HashMap<>();
        ChannelHandler ch;
        serversConnected = new HashMap<>();
        lastHeartbeat = new HashMap<>();
        chunkSize = new HashMap<>();
        fileNameMapping = new HashMap<>();
        connections = new HashMap<>();
        serversConnected.put(1,true);
        serversConnected.put(2,true);
        serversConnected.put(3,true);
        serversConnected.put(4,true);
        serversConnected.put(5,true);




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
                
                
                ch = new ChannelHandler(s[i]);
                dos[i] = ch.getOutputStreamObject();
                dis[i] = ch.getInputStreamObject();
                Thread t = new Thread(ch);
                t.start();
                System.out.print("Starting thread number" + i);
                logger.info("Starting thread number" + i);
                connections.put(i+1,s[i]);

            }
            // accept connections from clients
        }
        catch (IOException e) {
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
                Random rand = new Random();
                //temp hasmap will be replaced with servers connected will store NodeID and true false
                Map<Integer,Boolean> temp = new HashMap<>();


                Object msgObj = msgQueue.peek();

                if(msgObj instanceof heartbeatMessage){
                    heartbeatMessage msg = (heartbeatMessage) msgObj;
                    System.out.println("message received at metadataServer"+msg);

                    if(msg.getMsgtype() == MessageType.HEARTBEAT){

                        //todo when mserver receives replica size (offset) store the same size to chunk also
                        heartbeatMessage heartbeatmsg = (heartbeatMessage) msg;
                        //System.out.println("heartBeat message received from  server id :"+ msg.getSenderID());
                        int serverID = msg.getSenderID();
                        lastHeartbeat.put(serverID,System.currentTimeMillis() / 1000);
                        //todo data update as per sending
                        String linuxFileName;
                        String fileName, chunkName1; //not required
                        int senderID, chunkIndex, versionNo;
                        long offset;

                        heartbeat[] heartbeats = heartbeatmsg.getHeartBeats();
                        for(int i=0;i<heartbeats.length;i++){
                            //abc.txt
                        	fileName = heartbeats[i].getFileName()+".txt";
                            //abc_1_0_2.txt
                        	linuxFileName = heartbeats[i].getLinuxFileName();
                        	//2
                        	senderID= heartbeats[i].getSenderID()+1;
                        	
                        	String[] chunkSplit= linuxFileName.split("_");
                        	
                        	//abc_1
                        	chunkName1 = chunkSplit[0]+"_"+chunkSplit[1];
                            
                            
                            //todo check how server is naming chunk index
                        	//1
                            chunkIndex = heartbeats[i].getChunkindex();
                            offset = heartbeats[i].getOffset();
                            //0
                            versionNo = heartbeats[i].getVersion_num();
                           
                            

                            // check the code here
                            
                            linuxfileToChunks.putIfAbsent(fileName, new ArrayList<String>());
                            if(!linuxfileToChunks.get(fileName).contains(chunkName1)) {
                            	System.out.println(fileName+"linux file is under processing now --- "+linuxFileName+" ,senderID: "+
                                        senderID+", chunkIndex: "+chunkIndex+" ,versionNo: "+versionNo + "chunkName : "+chunkName1);
                            	
                            	linuxfileToChunks.get(fileName).add(chunkName1);
                            }
                            //linuxfileToChunks.put(fileName, linuxfileToChunks.get(fileName).add(chunkName));
                            
                            List<String> chunks = linuxfileToChunks.get(fileName);
                            System.out.println("chunk");
                            
                            String chunkName = chunks.get(chunkIndex-1);
                            chunkToReplicas.putIfAbsent(chunkName, new ArrayList<String>());
                            if(!chunkToReplicas.get(chunkName).contains(fileName)) {
                            	chunkToReplicas.get(chunkName).add(fileName);
                            	
                            	System.out.println(fileName+"linux file is under processing now --- "+linuxFileName+" ,senderID: "+
                                        senderID+", chunkIndex: "+chunkIndex+" ,versionNo: "+versionNo + "chunkName : "
                            			+chunkName1 +" and "+chunkName+" replica Name"+ fileName +" offset "+offset);
                            	
                            	
                            }
                            
                            
                            List<String> replicas = chunkToReplicas.get(chunkName);
                            int tempVersion = 0;
                            int version=0;
                            Message message;

                            for(String replica : replicas){
                            	
                            	replicaVersion.putIfAbsent(replica, 0);
                            	
                            	version = replicaVersion.get(replica);
                                if(version > tempVersion){
                                    tempVersion = version;
                                }
                                
                                System.out.println(fileName+"linux file is under processing now --- 2"+linuxFileName+" ,senderID: "+
                                        senderID+", chunkIndex: "+chunkIndex+" ,versionNo: "+versionNo + "chunkName : "
                            			+chunkName1 +" and "+chunkName+" replica Name"+ fileName +" offset "+offset);
                            	
                                
                                replicaToServer.putIfAbsent(replica, senderID);
                                
                                if(senderID == replicaToServer.get(replica)){
                                    //when servers was dead and got connected again
                                	
                                    if(!serversConnected.get(senderID)){
                                        if(version != tempVersion){
                                            message = new Message(1, MessageType.UPDATEREPLICA, serverID, replica, linuxFileName);
                                            Socket socket = connections.get(senderID);
                                            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                                            os.writeObject(message);
                                            //send message that you need to update the replicas
                                        }
                                    }

                                    replicaVersion.put(replica, versionNo);
                                    replicaSize.put(replica, offset);
                                    chunkSize.put(chunkName,offset); //to check the previous chunck is same size and then store. future reff
                                }
                            }
                        }
                        serversConnected.put(serverID,true);
                    }

                    msgQueue.remove();

                }
                else if(msgObj instanceof Message) {
                    Message msg = (Message) msgObj;
                    msgQueue.remove();
                    System.out.println("message received at metadataServer"+msg);

                    if(msg.getMsgtype() == MessageType.CREATE){
                        System.out.println("Create message received from  Client id :"+ msg.getSenderID());
                        // 3 server selected to create a linux file with with chunk 1
                        List<Integer> serversSelected = randomServers();
                        serversSelected.forEach(i -> System.out.println("seleted server for create request"+i));

                        String linuxFilename = msg.getFileName();
                        System.out.println("file name"+msg.getFileName());
                        String[] tempLinuxFileName = linuxFilename.split("\\.");

                        String chunkName;
                        int tempVal;

                        if(fileNameMapping.containsKey(linuxFilename)){
                            tempVal= fileNameMapping.get(linuxFilename);
                            chunkName = tempLinuxFileName[0]+"_"+(tempVal++)+"."+tempLinuxFileName[1];
                            fileNameMapping.put(linuxFilename,tempVal);
                        }else {
                            tempVal = 1;
                            chunkName = tempLinuxFileName[0]+"_"+(tempVal++)+"."+tempLinuxFileName[1];
                            fileNameMapping.put(linuxFilename,tempVal);
                            System.out.println("chunk name for new created file"+chunkName);
                            linuxfileToChunks.put(linuxFilename,new ArrayList<String>());
                        }

                        //below code will be used for future purpose.
                        linuxfileToChunks.get(linuxFilename).add(chunkName);
                        
                        
                        
                        for(int i : serversSelected) {
						    /*it will call the function to send message to create chunk to the selected server where i
						    is ID of server
                            */
                            if(serversConnected.get(i)) {
                                String replicaName = chunkName.split("\\.")[0] +"_"+ "0"+ "_" + i +"."+tempLinuxFileName[1];
                                System.out.println("replica name for new created file"+replicaName);
                                sendMessageToServer(i-1, MessageType.CREATECOMMAND, chunkName, linuxFilename, replicaName,  (long) 0);
                            }
                            else{
                                System.out.println("server is dead, ID:" + i);
                            }


                        }

                    }


                    if(msg.getMsgtype() == MessageType.APPEND){
                        System.out.println("Append message received from  Client id :"+ msg.getSenderID());
                        String linuxFileName = msg.getFileName();
                        List<String> chunksNames = linuxfileToChunks.get(linuxFileName);
                        String lastChunk = chunksNames.get(chunksNames.size()-1);
                        List<String> replicasName = chunkToReplicas.get(lastChunk);
                        List<Integer> replicasToServers = new ArrayList<>();
                        List<String> replicas = new ArrayList<>();
                        int clientId = msg.getSenderID();



                        for(String replica : replicasName){
                            int serverID = replicaToServer.get(replica);
                            if(serversConnected.get(serverID)) {
                                replicasToServers.add(serverID);
                                replicas.add(replica);

                            }

                        }

                        replicasToServers.forEach(i-> System.out.println("replicas to servers" + i));
                        replicas.forEach(i-> System.out.println("replicas list:  "+i));

                        Message message = new Message(0, MessageType.APPENDRESPONSE,
                                replicaToServer.get(0),replicaToServer.get(1),replicaToServer.get(2),
                                replicas.get(0),replicas.get(1),replicas.get(2),linuxFileName);

                        System.out.println("sending message to do the append to client id: "+clientId);
                        Socket socket = connections.get(clientId);
                        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                        os.writeObject(message);


                    }


                    else if(msg.getMsgtype() == MessageType.READ){
                        System.out.println("Read message received from  Client id :"+ msg.getSenderID());
                        String linuxFileName = msg.getFileName();
                        long offset = msg.getOffset();
                        System.out.println(" offset : "+offset);
                        //check with KD
                        List<String> chunks = linuxfileToChunks.get(linuxFileName);
                        System.out.println("linux file name "+linuxFileName);
                        chunks.forEach(i-> System.out.println("chunk names : "+i));
                        Long size = (long)0;
                        String replicaToRead;
                        int serverID;

                        for(String chunk : chunks){
                            size = size+chunkSize.get(chunk);

                            System.out.println("chunk size updated : "+size);
                            if(offset< size){
                                //return that file
                                replicaToRead = chunkToReplicas.get(chunk).get(0);
                                System.out.println("replica name for the read : "+replicaToRead);
                                serverID = replicaToServer.get(replicaToRead);
                                System.out.println("server id that is being used+ "+serverID);

                                sendMessageToServer(serverID,MessageType.READRESPONSE,chunk,linuxFileName,replicaToRead,
                                        (long)0);

                                break;
                            }
                        }
                    }



                }






                //send servers that you are outdated!
                //And tell which files are outdated!



            }
        }

    }



    class ChannelHandler implements Runnable {
        ObjectInputStream datainput;
        ObjectOutputStream dataoutput;

        Socket socket;

        public ChannelHandler(Socket s) {
            try {
                // initializing data i/p and o/p streams
                datainput = new ObjectInputStream(s.getInputStream());
                dataoutput = new ObjectOutputStream(s.getOutputStream());
                dataoutput.flush();
                dataoutput.reset();
                
        

                System.out.print("after socket initialization" + datainput + " " + dataoutput + " " + s);
                logger.info("After socket initialization" + datainput + " " + dataoutput + " " + s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public ObjectOutputStream getOutputStreamObject() {
        	return dataoutput;
        }
        
        public ObjectInputStream getInputStreamObject() {
        	return datainput;
        }
        
        public void run() {
            try {
            	Thread.sleep(3000);

                while(true) {
                	
                    if (true)
                    {
                    	//System.out.println("message received --1 ");
                        Object msg = datainput.readObject();
                        //System.out.println("message received --2"+msg);
                        dataoutput.reset();

                        msgQueue.add(msg);


                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }


    }

    private void sendMessageToServer(int serverID, MessageType type, String chunkName, String linuxFileName ,
                                     String replicaName, Long size){


        try {
            System.out.println("sending message to server to create file ---- Create/Append/Read file step 1");
            Socket socket = connections.get(serverID);
            //ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            

            Message message = new Message();
            message.setFileName(linuxFileName);
            message.setChunkname(replicaName);
            message.setMsgtype(type);
            message.setSenderID(1);
            message.setServer(serverID);
            dos[serverID].writeObject(message);
            dos[serverID].reset();
            
            //os.close();
            //socket.close();
            
            //dos[serverID].writeObject(message);

                if(type == MessageType.CREATE){
                    linuxfileToChunks.putIfAbsent(linuxFileName, new ArrayList<>());
                    updateMetaData(chunkName, serverID, linuxFileName, replicaName , size);
                }

            System.out.println("sending message to server to create file ---- Create/Append/Read file step 2");

        } catch (Exception e) {
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
            rand = randomNumber.nextInt(list.size());
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
            e.printStackTrace();
        }
    }

}
