import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class createFile {

	public static void filecreate(String filename) throws IOException
	{
		RandomAccessFile f;
		double bytes = 0;
		try {
			f = new RandomAccessFile("./files/"+filename, "rw");
			f.setLength(4096*1024);//4096*1024
			long l = f.getFilePointer();
			System.out.println("current offset"+l);
		    bytes = f.length();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		System.out.println("file "+filename+"created with file size:"+megabytes);
		
	}
	public static void filewithString(String filename,String filechars) throws IOException
	{
		RandomAccessFile f;
		double bytes = 0;
		try {
			f = new RandomAccessFile("./files/"+filename, "rw");
			f.setLength(4096*1024);//4096*1024
			long l = f.getFilePointer();
			System.out.println("current offset"+l);
		    bytes = f.length();
		    System.out.println("size of write"+filechars.getBytes().length);
	        f.writeBytes(filechars);
		    System.out.println("offset after write"+f.getFilePointer());
		    long l2 = f.getFilePointer();
			System.out.println("current offset after write"+l2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static boolean fileAppendString(String filename,String filechars) throws IOException
	{
		boolean result=false; 
		RandomAccessFile f;
		double bytes = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile("./files/"+filename, "rw");
			raf.seek(raf.length());
		//	int leftspace = 4096 -(int)raf.length();
			if(!filechars.equals("NULL"))
			{
			raf.write(filechars.getBytes());
			System.out.println("append op done pointer at"+raf.getFilePointer());
			result=true;
			}
			
			else
			{
				
				raf.write(null);
				result=false;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;	
		
	}
	
	public static boolean checkfileAppend(String filename,int lengthofappend) throws IOException
	{
		boolean result=false; 
		RandomAccessFile f;
		double bytes = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile("./files/"+filename, "rw");
			int remaining = 4096-(int)raf.length();
			if(remaining >lengthofappend)
			{
				result=true;
				
			}
		else
		{
			result = false;
			
		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;	
		
	}
	
}
