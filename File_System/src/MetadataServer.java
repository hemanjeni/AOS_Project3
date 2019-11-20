
//package testing;
import java.io.*;
import java.net.*;
import java.util.*;
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

	public MetadataServer(String args[]) throws IOException {

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