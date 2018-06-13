import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class P2PChatIn extends Thread {
	public UserInfo chatUser; //聊天对象的信息
	public Socket socket; //socket通信对象
	
	public P2PChatIn(Socket socket, String chatUserName) {
		this.socket = socket;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), chatUserName);
	}
	
	public P2PChatIn(Socket socket) {
		this.socket = socket;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), "default");
	}
	
	public void run() {
		InputStream is; //socket的输入流
		try {
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while (true) {
				while (br.ready()) {
					String content = br.readLine();
					System.out.println(this.chatUser.username+":"+content);
				}
				Thread.sleep(500);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
