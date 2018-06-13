import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class P2PChatOut extends Thread {
	public UserInfo chatUser; //聊天对象的信息
	public Socket socket; //socket通信对象
	
	public P2PChatOut(Socket socket, String chatUserName) {
		this.socket = socket;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), chatUserName);
	}
	
	public P2PChatOut(Socket socket) {
		this.socket = socket;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), "default");
	}
	
	public void run() {
		OutputStream os;
		try {
			os = socket.getOutputStream(); //socket的输出流
			while (true) {
				//os.write(loginCommand.getBytes("US-ASCII"));
				sleep(500);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
