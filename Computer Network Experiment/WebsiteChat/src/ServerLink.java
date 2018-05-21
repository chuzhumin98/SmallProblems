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
			
			String loginCommand = ServerLink.studentID+"_net2018\r\n";
			os.write(loginCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send login request!");
			
			String loginResponse = br.readLine();
			System.out.println(loginResponse);
			
			String logoutCommand = "logout"+ServerLink.studentID+"\r\n";
			os.write(logoutCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send logout request!");
			
			String logoutResponse = br.readLine();
			System.out.println(logoutResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
