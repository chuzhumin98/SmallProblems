import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerLink {
	//public static String serverIP = "166.111.140.84"; //服务器的IP地址
	//public static String serverIP = "166.111.140.52"; //服务器的IP地址
	public static String serverIP = "localhost"; //服务器的IP地址
	public static int serverPort = 8000; //服务器端的端口号
	public static String studentID = "2015012177";
	
	
	public static void main(String[] args) {
		Socket socket;
		try {
			socket = new Socket(ServerLink.serverIP, ServerLink.serverPort); //新建一个socket连接对象	
			
			OutputStream os = socket.getOutputStream(); //socket的输出流
			
			InputStream is = socket.getInputStream(); //socket的输入流
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			/** 登录系统  */
			String loginCommand = ServerLink.studentID+"_net2018";
			os.write(loginCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send login request!");
			
			char[] responseBuffer = new char[20];
			int flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve login message from server!");
			}
			String loginResponse = String.valueOf(responseBuffer);
			System.out.println(loginResponse);
			
			/** 查询好友IP */
			String queryCommand = "q2015012177";
			os.write(queryCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send query request!");
			
			responseBuffer = new char [20];
			flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve query message from server!");
			}
			String queryResponse = String.valueOf(responseBuffer);
			System.out.println(queryResponse);
			
			/** 登出系统 */
			String logoutCommand = "logout"+ServerLink.studentID;
			os.write(logoutCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send logout request!");
			
			responseBuffer = new char [20];
			flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve logout message from server!");
			}
			String logoutResponse = String.valueOf(responseBuffer);
			System.out.println(logoutResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
