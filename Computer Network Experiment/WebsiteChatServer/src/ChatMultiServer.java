import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatMultiServer {
	public static int clientNum = 0; //连接用户的个数
	public static ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	public static ArrayList<String> validIDs = new ArrayList<String>(); //可用的所有学生卡编号
	
	/**
	 * 读取所有的合法用户ID
	 * 
	 * @param path
	 */
	public static void readValidIDs(String path) {
		try {
			Scanner input = new Scanner(new File(path));
			while (input.hasNext()) {
				String thisID = input.next();
				validIDs.add(thisID);
			}
			System.out.println("has load all the "+validIDs.size()+" valid IDs.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ChatMultiServer.readValidIDs("resource/IDs.txt");
		ServerSocket ssocketWelcome;
		try {
			ssocketWelcome = new ServerSocket(8000);
			System.out.println("start for server engine.");
	        while(true){
	            Socket socketServer = ssocketWelcome.accept();
	            ChatMultiServer.clientNum++; //增加了一个用户
	            ChatServerThread thread = new ChatServerThread(socketServer);
	            thread.start();
	            
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
}
