
//package testing;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {

	static Map<String, Integer> hmap = new HashMap<>();
	static DataOutputStream dos[] = new DataOutputStream[7];
	static DataInputStream dis[] = new DataInputStream[7];
	static ServerSocket ss[] = new ServerSocket[7];
	static Socket s[] = new Socket[7];
	int server_no;
	static Thread[] t;
	
	Logger logger;

	public Server(String args[]) throws IOException {
		ConfigProperties prop = new ConfigProperties();
		server_no = Integer.parseInt(args[0]);
		String metadataServeraddress = prop.getPropValues("metadataServeraddress");
		int metadataServeraddressforserver1 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver1"));
		int metadataServeraddressforserver2 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver2"));
		int metadataServeraddressforserver3 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver3"));
		int metadataServeraddressforserver4 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver4"));
		int metadataServeraddressforserver5 =  Integer.parseInt(prop.getPropValues("metadataServeraddressforserver5"));
		
		String server1Address =  prop.getPropValues("Server1Address");
		String server2Address =  prop.getPropValues("Server2Address");
		String server3Address =  prop.getPropValues("Server3Address");
		String server4Address =  prop.getPropValues("Server4Address");
		String server5Address =  prop.getPropValues("Server5Address");
		int	server1portforserver2=  Integer.parseInt(prop.getPropValues("server1portforserver2"));
		int server1portforserver3=  Integer.parseInt(prop.getPropValues("server1portforserver3"));
		int server1portforserver4=  Integer.parseInt(prop.getPropValues("server1portforserver4"));
		int server1portforserver5=  Integer.parseInt(prop.getPropValues("server1portforserver5"));
		int server2portforserver3=  Integer.parseInt(prop.getPropValues("server2portforserver3"));
		int server2portforserver4=  Integer.parseInt(prop.getPropValues("server2portforserver4"));
		int server2portforserver5=  Integer.parseInt(prop.getPropValues("server2portforserver5"));
		int server3portforserver4=  Integer.parseInt(prop.getPropValues("server3portforserver4"));
		int server3portforserver5=  Integer.parseInt(prop.getPropValues("server3portforserver5"));
		int server4portforserver5=  Integer.parseInt(prop.getPropValues("server4portforserver5"));
	
		
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
			logger = Logger.getLogger(server_no + "server.log");

			FileHandler fh = new FileHandler("logs/" + server_no + "-server.log");
			fh.setFormatter(new SimpleFormatter());
			// fh.setLevel(logLevel);
			logger.addHandler(fh);
			logger.info("Server" + server_no + "initialization");
			// waiting for incoming socket connections from clients
		
				//server no=1
				if ((server_no) == 1) 
				{
					//connect to metadata server
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforserver1);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					// connecting to other servers
					for (int i = 1; i < 5; i++) 
					{
						int serverport =0;
						if(i==1)
						{
							serverport=server1portforserver2;
						}
						if(i==2)
						{
							serverport=server1portforserver3;
						}
						if(i==3)
						{
							serverport=server1portforserver4;
						}
						if(i==4)
						{
							serverport=server1portforserver5;
						}
						
					ss[i] = new ServerSocket(serverport);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());

					t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					}
					
					//accept client connections
					ss[5] = new ServerSocket(server1portforclient1);
					s[5] = ss[5].accept();
					dos[5] = new DataOutputStream(s[5].getOutputStream());
					dis[5] = new DataInputStream(s[5].getInputStream());

					t[5] = new Thread(new ChannelHandler(s[5]));
					t[5].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 5);
					
					ss[6] = new ServerSocket(server1portforclient2);
					s[6] = ss[6].accept();
					dos[6] = new DataOutputStream(s[6].getOutputStream());
					dis[6] = new DataInputStream(s[6].getInputStream());

					t[6] = new Thread(new ChannelHandler(s[6]));
					t[6].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 6);
					
				}
				//server no=2
				if ((server_no) == 2) {
					//connect to metadata server
					
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforserver2);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					s[1] = new Socket(server1Address, server1portforserver2);
					dos[1] = new DataOutputStream(s[1].getOutputStream());
					dis[1] = new DataInputStream(s[1].getInputStream());
					t[1] = new Thread(new ChannelHandler(s[1]));
					//start thread for each server
					t[1].start();
					//connect to server1 first before accepting connections from other clients
					
					
					for (int i = 2; i < 5; i++) 
					{
						int serverport =0;
						
						if(i==2)
						{
							serverport=server2portforserver3;
						}
						if(i==3)
						{
							serverport=server2portforserver4;
						}
						if(i==4)
						{
							serverport=server2portforserver5;
						}
					ss[i] = new ServerSocket(serverport);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);

					 t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					}
					//accept client connections
					ss[5] = new ServerSocket(server2portforclient1);
					s[5] = ss[5].accept();
					dos[5] = new DataOutputStream(s[5].getOutputStream());
					dis[5] = new DataInputStream(s[5].getInputStream());

					t[5] = new Thread(new ChannelHandler(s[5]));
					t[5].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 5);
					
					ss[6] = new ServerSocket(server2portforclient2);
					s[6] = ss[6].accept();
					dos[6] = new DataOutputStream(s[6].getOutputStream());
					dis[6] = new DataInputStream(s[6].getInputStream());

					t[6] = new Thread(new ChannelHandler(s[6]));
					t[6].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 6);
					
				}
				//server no=3
				if ((server_no) == 3) {
					
					//connect to metadata server
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforserver3);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					s[1] = new Socket(server1Address, server1portforserver3);
					dos[1] = new DataOutputStream(s[1].getOutputStream());
					dis[1] = new DataInputStream(s[1].getInputStream());
					t[1] = new Thread(new ChannelHandler(s[1]));
					//start thread for each server
					t[1].start();
					
					s[2] = new Socket(server1Address, server2portforserver3);
					dos[2] = new DataOutputStream(s[2].getOutputStream());
					dis[2] = new DataInputStream(s[2].getInputStream());
					t[2] = new Thread(new ChannelHandler(s[2]));
					//start thread for each server
					t[2].start();
					
					
					for (int i = 3; i < 5; i++) 
					{
                        int serverport =0;
						
						
						if(i==3)
						{
							serverport=server3portforserver4;
						}
						if(i==4)
						{
							serverport=server3portforserver5;
						}
					ss[i] = new ServerSocket(serverport);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					}
					//accept client connections
					ss[5] = new ServerSocket(server3portforclient1);
					s[5] = ss[5].accept();
					dos[5] = new DataOutputStream(s[5].getOutputStream());
					dis[5] = new DataInputStream(s[5].getInputStream());

					t[5] = new Thread(new ChannelHandler(s[5]));
					t[5].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 5);
					
					ss[6] = new ServerSocket(server3portforclient2);
					s[6] = ss[6].accept();
					dos[6] = new DataOutputStream(s[6].getOutputStream());
					dis[6] = new DataInputStream(s[6].getInputStream());

					t[6] = new Thread(new ChannelHandler(s[6]));
					t[6].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 6);
				}
				//server no=4
				if ((server_no) == 4) {
					
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforserver4);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					s[1] = new Socket(server1Address, server1portforserver4);
					dos[1] = new DataOutputStream(s[1].getOutputStream());
					dis[1] = new DataInputStream(s[1].getInputStream());
					t[1] = new Thread(new ChannelHandler(s[1]));
					//start thread for each server
					t[1].start();
					s[2] = new Socket(server2Address, server2portforserver4);
					dos[2] = new DataOutputStream(s[2].getOutputStream());
					dis[2] = new DataInputStream(s[2].getInputStream());
					t[2] = new Thread(new ChannelHandler(s[2]));
					//start thread for each server
					t[2].start();
					s[3] = new Socket(server3Address, server3portforserver4);
					dos[3] = new DataOutputStream(s[3].getOutputStream());
					dis[3] = new DataInputStream(s[3].getInputStream());
					t[3] = new Thread(new ChannelHandler(s[3]));
					//start thread for each server
					t[3].start();
					for (int i = 4; i < 5; i++) 
					{
						 int serverport =0;
							
							if(i==4)
							{
								serverport=server4portforserver5;
							}
					ss[i] = new ServerSocket(serverport);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					 t[i] = new Thread(new ChannelHandler(s[i]));
					t[i].start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
					}
					//accept client connections
					ss[5] = new ServerSocket(server4portforclient1);
					s[5] = ss[5].accept();
					dos[5] = new DataOutputStream(s[5].getOutputStream());
					dis[5] = new DataInputStream(s[5].getInputStream());

					t[5] = new Thread(new ChannelHandler(s[5]));
					t[5].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 5);
					
					ss[6] = new ServerSocket(server4portforclient2);
					s[6] = ss[6].accept();
					dos[6] = new DataOutputStream(s[6].getOutputStream());
					dis[6] = new DataInputStream(s[6].getInputStream());

					t[6] = new Thread(new ChannelHandler(s[6]));
					t[6].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 6);
				}
				//server no=5
				if ((server_no) == 5) {
					
					s[0] = new Socket(metadataServeraddress, metadataServeraddressforserver5);
					dos[0] = new DataOutputStream(s[0].getOutputStream());
					dis[0] = new DataInputStream(s[0].getInputStream());
					t[0] = new Thread(new ChannelHandler(s[0]));
					//start thread for each server
					t[0].start();
					
					
					s[1] = new Socket(server1Address, server1portforserver5);
					dos[1] = new DataOutputStream(s[1].getOutputStream());
					dis[1] = new DataInputStream(s[1].getInputStream());
					t[1] = new Thread(new ChannelHandler(s[1]));
					//start thread for each server
					t[1].start();
					s[2] = new Socket(server2Address, server2portforserver5);
					dos[2] = new DataOutputStream(s[2].getOutputStream());
					dis[2] = new DataInputStream(s[2].getInputStream());
					t[2] = new Thread(new ChannelHandler(s[2]));
					//start thread for each server
					t[2].start();
					s[3] = new Socket(server3Address, server3portforserver5);
					dos[3] = new DataOutputStream(s[3].getOutputStream());
					dis[3] = new DataInputStream(s[3].getInputStream());
					t[3] = new Thread(new ChannelHandler(s[3]));
					//start thread for each server
					t[3].start();
					s[4] = new Socket(server4Address, server4portforserver5);
					dos[4] = new DataOutputStream(s[4].getOutputStream());
					dis[4] = new DataInputStream(s[4].getInputStream());
					t[4] = new Thread(new ChannelHandler(s[4]));
					//start thread for each server
					t[4].start();
					
					ss[5] = new ServerSocket(server5portforclient1);
					s[5] = ss[5].accept();
					dos[5] = new DataOutputStream(s[5].getOutputStream());
					dis[5] = new DataInputStream(s[5].getInputStream());

					t[5] = new Thread(new ChannelHandler(s[5]));
					t[5].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 5);
					
					ss[6] = new ServerSocket(server5portforclient2);
					s[6] = ss[6].accept();
					dos[6] = new DataOutputStream(s[6].getOutputStream());
					dis[6] = new DataInputStream(s[6].getInputStream());

					t[6] = new Thread(new ChannelHandler(s[6]));
					t[6].start();
					System.out.print("Starting thread number" + 5);
					logger.info("Starting thread number" + 6);
				}
				
			

			System.out.print("connected to all clients");
			logger.info("connected to all clients");
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
		new Server(args);
	}

}