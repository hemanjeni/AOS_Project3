
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