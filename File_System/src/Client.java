
//package testing;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client {

	static Map<String, Integer> hmap = new HashMap<>();
	static DataOutputStream dos[] = new DataOutputStream[7];
	static DataInputStream dis[] = new DataInputStream[7];
	static ServerSocket ss[] = new ServerSocket[7];
	static Socket s[] = new Socket[7];
	int client_no;
	static Thread[] t;
	
	Logger logger;

	public Client(String args[]) throws IOException {
		ConfigProperties prop = new ConfigProperties();
		client_no = Integer.parseInt(args[0]);
		String metadataServeraddress = prop.getPropValues("metadataServeraddress");
		
		String server1Address =  prop.getPropValues("Server1Address");
		String server2Address =  prop.getPropValues("Server2Address");
		String server3Address =  prop.getPropValues("Server3Address");
		String server4Address =  prop.getPropValues("Server4Address");
		String server5Address =  prop.getPropValues("Server5Address");
			
		int metadataServeraddressforclient1 = Integer.parseInt(prop.getPropValues("metadataServeraddressforclient1"));
		int metadataServeraddressforclient2 = Integer.parseInt(prop.getPropValues("metadataServeraddressforclient2"));
		
		// server ports for client 1 ->10001 client 2---> 10002
		int server1portforclient1 = Integer.parseInt(prop.getPropValues("server1portforclient1"));
		int server1portforclient2 = Integer.parseInt(prop.getPropValues("server1portforclient2"));
		int server2portforclient1 = Integer.parseInt(prop.getPropValues("server2portforclient1"));
		int server2portforclient2 = Integer.parseInt(prop.getPropValues("server2portforclient2"));
		int server3portforclient1 = Integer.parseInt(prop.getPropValues("server3portforclient1"));
		int server3portforclient2 = Integer.parseInt(prop.getPropValues("server3portforclient2"));
		int server4portforclient1 = Integer.parseInt(prop.getPropValues("server4portforclient1"));
		int server4portforclient2 = Integer.parseInt(prop.getPropValues("server4portforclient2"));
		int server5portforclient1 = Integer.parseInt(prop.getPropValues("server5portforclient1"));
		int server5portforclient2 = Integer.parseInt(prop.getPropValues("server5portforclient2"));

		try {
			//logger for server
			File logDir = new File("./logs/");
			if (!(logDir.exists()))
				logDir.mkdir();
			logger = Logger.getLogger(client_no + "client.log");

			FileHandler fh = new FileHandler("logs/" + client_no + "-client.log");
			fh.setFormatter(new SimpleFormatter());
			// fh.setLevel(logLevel);
			logger.addHandler(fh);
			logger.info("Client" + client_no + "initialization");
			// waiting for incoming socket connections from clients
		
				//server no=1
				if ((client_no) == 1) 
				{
					//connect to metadata server
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforclient1);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					// connecting to other servers
					for (int i = 1; i <=5; i++) 
					{
						String serveraddress="";
						if(i==1)
						{
							serveraddress= 	server1Address;
						}
						if(i==2)
						{
							serveraddress= 	server2Address;
						}
						if(i==3)
						{
							serveraddress= 	server3Address;
						}
						if(i==4)
						{
							serveraddress= 	server4Address;
						}
						if(i==5)
						{
							serveraddress= 	server5Address;
						}
					s[i] = new Socket(serveraddress,10001);
					
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());

					t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					}
					
				
					
				}
				//server no=2
				if ((client_no) == 2) {
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforclient2);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					// connecting to other servers
					for (int i = 1; i <=5; i++) 
					{
						String serveraddress="";
						if(i==1)
						{
							serveraddress= 	server1Address;
						}
						if(i==2)
						{
							serveraddress= 	server2Address;
						}
						if(i==3)
						{
							serveraddress= 	server3Address;
						}
						if(i==4)
						{
							serveraddress= 	server4Address;
						}
						if(i==5)
						{
							serveraddress= 	server5Address;
						}
					s[i] = new Socket(serveraddress,10001);
					
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());

					t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					
					}
					
				}
				//server no=3
			logger.info("connected to metadata server and servers");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
//  threads handling each client
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

				
				}
			 catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/* server number to be given as argument while running */
	public static void main(String[] args) throws IOException {
		new Client(args);
	}

}