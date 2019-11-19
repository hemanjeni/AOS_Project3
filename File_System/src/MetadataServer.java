
//package testing;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

	public MetadataServer(String args[]) throws IOException {

		new PriorityBlockingQueue<Message>();
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

			// accept connections from clients
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// will get the message here


		while(true){

			if(msgQueue.size() !=0){
				Message msg = msgQueue.peek();
				msgQueue.remove();
				Random rand = new Random();

				//temp hasmap will be replaced with servers connected
				Map<Integer,String> temp = new HashMap<>();


				if(msg.getMsgtype() == MessageType.Create){
					List<Integer> serversSelected = randomServers();
					String chunkName = msg.getFileName()+rand.nextInt(100);

					//below code will be used for future purpose.

					/*for(int i : serversSelected) {
						if (temp.get(i)) {
							sendMessageToServer(temp.get(i), MessageType.class, chunkName);
							updateMedaData(msg.getFileName(),chunkName, temp.get(i));

						}

					}*/

				}
			}
		}

	}


	private void updateMedaData(String fileName, String chunkName, int ServerID){
		// update the information

	}

	private void sendMessageToServer(){

		//todo
		String address;
		int port;


	}

	private void sendMessageToServer(int serverID, MessageType type, String chunk_name){


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}