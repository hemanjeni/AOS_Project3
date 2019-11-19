
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

	public Server(String args[]) {
		server_no = Integer.parseInt(args[0]);

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
			for (int i = 0; i < 7; i++) {
				//server no=1
				if ((server_no) == 1) {
					ss[i] = new ServerSocket(5001 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());

					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server no=2
				if ((server_no) == 2) {
					ss[i] = new ServerSocket(5011 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);

					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server no=3
				if ((server_no) == 3) {
					ss[i] = new ServerSocket(5021 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server no=4
				if ((server_no) == 4) {
					ss[i] = new ServerSocket(5031 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server no=5
				if ((server_no) == 5) {
					ss[i] = new ServerSocket(5041 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server_no=6
				if ((server_no) == 6) {
					ss[i] = new ServerSocket(5051 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}
				//server no=7
				if ((server_no) == 7) {
					ss[i] = new ServerSocket(5061 + i);
					s[i] = ss[i].accept();
					dos[i] = new DataOutputStream(s[i].getOutputStream());
					dis[i] = new DataInputStream(s[i].getInputStream());
					System.out.print("Server port created at " + ss[i] + "" + s[i]);
					logger.info("Server port created at " + ss[i] + "" + s[i]);
					Thread t = new Thread(new ChannelHandler(s[i]));
					t.start();
					System.out.print("Starting thread number" + i);
					logger.info("Starting thread number" + i);
				}

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
	public static void main(String[] args) {
		new Server(args);
	}

}