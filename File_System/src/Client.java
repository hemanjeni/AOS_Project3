
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

	static Map<String, Integer> hmap_streams = new HashMap<>();// not required for now
	static DataOutputStream dos[] = new DataOutputStream[6];
	static DataInputStream dis[] = new DataInputStream[6];
	static ObjectOutputStream oos[] = new ObjectOutputStream[6];
	static ObjectInputStream ois[] = new ObjectInputStream[6];
	static Socket s[] = new Socket[6];
	static int client_no;
	static Thread[] t;
	public boolean exit=false;
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
				oos[0] = new ObjectOutputStream(s[0].getOutputStream());
				ois[0] = new ObjectInputStream(s[0].getInputStream());
				Thread t = new Thread(new ChannelHandler(s[0]));
				//start thread for each server
				t.start();

				// connecting to other servers
				for (int i = 1; i <=5; i++) 
				{
					String serveraddress="";
					int port =0;
					if(i==1)
					{
						serveraddress= 	server1Address;
						port = server1portforclient1;
					}
					if(i==2)
					{
						serveraddress= 	server2Address;
						port = server2portforclient1;
					}
					if(i==3)
					{
						serveraddress= 	server3Address;
						port = server3portforclient1;
					}
					if(i==4)
					{
						serveraddress= 	server4Address;
						port = server4portforclient1;
					}
					if(i==5)
					{
						serveraddress= 	server5Address;
						port = server5portforclient1;
					}
					s[i] = new Socket(serveraddress,port);

					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					oos[i] = new ObjectOutputStream(s[i].getOutputStream());
					ois[i] = new ObjectInputStream(s[i].getInputStream());
					Thread t2 = new Thread(new ChannelHandler(s[i]));
					t2.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}



			}
			//server no=2
			if ((client_no) == 2) {
				s[0] = new Socket(metadataServeraddress, metadataServeraddressforclient2);
				dos[0] = new DataOutputStream(s[0].getOutputStream());
				dis[0] = new DataInputStream(s[0].getInputStream());
				oos[0] = new ObjectOutputStream(s[0].getOutputStream());
				ois[0] = new ObjectInputStream(s[0].getInputStream());
				Thread t = new Thread(new ChannelHandler(s[0]));
				//start thread for each server
				t.start();

				// connecting to other servers
				for (int i = 1; i <=5; i++) 
				{
					String serveraddress="";
					int port =0;
					if(i==1)
					{
						serveraddress= 	server1Address;
						port = server1portforclient2;
					}
					if(i==2)
					{
						serveraddress= 	server2Address;
						port = server2portforclient2;
					}
					if(i==3)
					{
						serveraddress= 	server3Address;
						port = server3portforclient2;
					}
					if(i==4)
					{
						serveraddress= 	server4Address;
						port = server4portforclient2;
					}
					if(i==5)
					{
						serveraddress= 	server5Address;
						port = server5portforclient2;
					}
					s[i] = new Socket(serveraddress,port);

					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					oos[i] = new ObjectOutputStream(s[i].getOutputStream());
					ois[i] = new ObjectInputStream(s[i].getInputStream());
					Thread t2 = new Thread(new ChannelHandler(s[i]));
					t2.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);

				}

			}
			//server no=3
			logger.info("connected to metadata server and servers");
		}catch (Exception e) {
			e.printStackTrace();

		}
		String message2 = "";
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader2 = new BufferedReader(input);
		System.out.println("Please enter operations  -1. CREATE 2. READ 3.APPEND  4.EXIT");

		try {
			logger.info("Listening to console");

			while (exit==false&&(message2 = reader2.readLine()) != null) {

				if (message2.equals("1")) {
					logger.info(message2);
					logger.info("create command to metadata server");
					System.out.println("Give <filename> and press enter");
					logger.info(	"Give <filename> and press enter");
					message2=reader2.readLine();
					Message m = new Message(client_no,MessageType.CREATE,message2);
					oos[0].writeObject(m);
					System.out.println("create command sent to MS");
					logger.info(	"create command sent to MS");
					//DataOutputStream 	out    = new DataOutputStream(s[0].getOutputStream());
					//out.writeUTF("CREATE" + "," +message2+","+ client_no);
				}



				if (message2.equals("2")) {

					logger.info("read command to metadata server");
					System.out.println("Give <filename> and press enter");
					logger.info(	"Give <filename> and press enter");
					String fileName = reader2.readLine();
					System.out.println("Give <offset> and press enter");
					logger.info(	"Give <offset> and press enter");
					String offset = reader2.readLine();
					int offset_num = Integer.parseInt(offset);
					//					pw5.write("READ" + "," + node_no + "," + fileName);
					Message m = new Message(client_no,MessageType.READ,fileName,offset_num);
					oos[0].writeObject(m);
					System.out.println("create command sent to MS");
					logger.info(	"create command sent to MS");


				}


				if (message2.equals("3")) {
					logger.info("append command to metadata server");
					System.out.println("Give <filename> and press enter");
					logger.info(	"Give <filename> and press enter");
					String fileName = reader2.readLine();
					//					pw5.write("READ" + "," + node_no + "," + fileName);

					Message m = new Message(client_no,MessageType.APPEND,fileName);
					oos[0].writeObject(m);
					System.out.println("create command sent to MS");
					logger.info(	"create command sent to MS");
				}

				if (message2.equals("4")) {
					exit = true;
					logger.info("bye to everyone!");
					System.out.println("bye to everyone!");
				} 

			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			logger.info(e.toString());
		}
	}
	//  threads handling each client
	class ChannelHandler implements Runnable {
		DataInputStream datainput;
		DataOutputStream dataoutput;
		ObjectOutputStream oostream;
		ObjectInputStream oistream;
		Socket socket;

		public ChannelHandler(Socket s) {
			try {
				// initializing data i/p and o/p streams
				datainput = new DataInputStream(s.getInputStream());
				dataoutput = new DataOutputStream(s.getOutputStream());
				oostream = new ObjectOutputStream(s.getOutputStream());
				oistream = new ObjectInputStream(s.getInputStream());
				System.out.print("after socket initialization" + datainput + " " + dataoutput + " " + s);
				logger.info("After socket initialization" + datainput + " " + dataoutput + " " + s);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void run() {
			try {
				System.out.println("Inside run");
				System.out.println("ois - hope they are not null!"+ois);
				System.out.println("oos - hope they are not null!"+oos);
				while(true)	{
					if (datainput.available() > 0)
					{
						logger.info("data available");

						Message object = (Message) oistream.readObject();
						String messagetype =  object.getMsgtype().toString();
						if(messagetype.equals("READRESPONSE"))
						{


							String filename = object.getFileName();
							int server = object.getServer();
							String chunkname = object.getChunkname();
							int chunkoffset = object.getChunkoffset();
							// after retrieving the contents of the msg, send READFILE  to the corresponding server

							Message m = new Message (client_no,MessageType.READFILE,chunkname,chunkoffset);
							oos[server].writeObject(m);
							logger.info("sent read request to server "+server);
							System.out.println("sent read request to server "+server);


						}
						if(messagetype.equals("READFILERESPONSE"))
						{

                              // name weird but its ok!
							String READRESPONSE = object.getReadcharacters();
							
							logger.info("reading contents of server "+READRESPONSE);
							System.out.println("reading contents of server "+READRESPONSE);


						}
						if(messagetype.equals("APPENDRESPONSE"))
						{
							//this.senderID = senderUID;
							//  this.msgtype = Msgtype;
							// this.fileName = FileName;
							// this.server1= Integer.parseInt(server1);
							// this.server2= Integer.parseInt(server2);
							// this.server3= Integer.parseInt(server3);
							//this.chunkname1= chunkname1;
							// this.chunkname2= chunkname2;
							//this.chunkname3= chunkname3; 
							// this.chunkoffset = chunkoffset; 
							String filename = object.getFileName();

							boolean result =	 initiate2Phase.initiate(object,ois,oos,client_no);
							if(result==true)
							{
								logger.info("2 phase commit implemented successfully");
								System.out.println("2 phase commit implemented successfully");	
								Message m = new Message(client_no,MessageType.APPENDCOMPLETE,filename) ;
								oos[0].writeObject(m);

								logger.info("sent append complete to metadataserver");
								System.out.println("sent append complete to metadataserver");	
							}
							else
							{
								logger.info("2 phase commit cannot be completed");
								System.out.println("2 phase commit cannot be completed");
								Message m = new Message(client_no,MessageType.APPENDNOTCOMPLETE,filename) ;
								logger.info("sent append incomplete to metadataserver");
								System.out.println("sent append incomplete to metadataserver");	
							}


						}
						if(messagetype.equals("CREATERESPONSE"))
						{
							//this.senderID = senderUID;
							//this.msgtype = Msgtype;
							//this.fileName = FileName;
							//this.server1= Integer.parseInt(server1);
							//this.server2= Integer.parseInt(server2);
							//this.server3= Integer.parseInt(server3);
							//this.chunkname1= chunkname1;
							//this.chunkname2= chunkname2;
							//this.chunkname3= chunkname3; 
							
							String filename = object.getFileName();
							int server1=object.getServer1();
							int server2=object.getServer2();
							int server3=object.getServer3();
							String filename1 = object.getChunkname1();
							String filename2 = object.getChunkname2();
							String filename3 = object.getChunkname3();
							logger.info("file"+" "+filename+"created at servers"+server1+","+server2+","+server3+".");
							System.out.println("file"+" "+filename+"created at servers"+server1+","+server2+","+server3+".");
							logger.info("filenames are "+" "+filename1+","+server1+","+filename2+","+filename3+"respectively.");
							System.out.println("filenames are "+" "+filename1+","+server1+","+filename2+","+filename3+"respectively.");


						}


					}
				}

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