import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ChatServerThread extends Thread{
	public Socket socket;
	public String username; //用户名
	public String IP; //IP地址
	public int port; //端口号
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;

	}
	
	public void run() {
		InputStream is;
		this.IP = this.socket.getInetAddress().getHostAddress();
		this.port = this.socket.getPort();
		System.out.println("IP:"+this.IP);
		System.out.println("Port:"+this.port);
		try {
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			OutputStream os = socket.getOutputStream(); //socket的输出流
	
			while (true) {
				String command = br.readLine();
				System.out.println("receive request:"+command);
				//判断是否满足登录的语法要求，如果满足则回复"lol"
				if (command.length() >= 18) {
					this.username = command.substring(0, 10);
					String subCommand = command.substring(10, 18);
					if (subCommand.equals("_net2018")) {
						String loginResponse = "lol\r\n";
						os.write(loginResponse.getBytes("US-ASCII"));
						System.out.println("succeed to send login response to "+this.username+"!");
						break;
					}
				}
			}
			
			while (true) {
				String command = br.readLine();
				System.out.println("receive request:"+command);
				if (command.length() >= 11) {
					if (command.charAt(0) == 'q') {
						String queryUserName = command.substring(1, 11);
						System.out.println(this.username+" has queried for "+queryUserName);
					}
				}
				if (command.length() >= 16) {
					if (command.substring(0, 6).equals("logout")) {
						String logoutResponse = "loo\r\n";
						os.write(logoutResponse.getBytes("US-ASCII"));
						System.out.println("succeed to send logout response to "+this.username+"!");
						break;
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //socket的输入流
		
		
	}
}
