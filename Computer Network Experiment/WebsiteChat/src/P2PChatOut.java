import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class P2PChatOut extends Thread {
	public UserInfo chatUser; //聊天对象的信息
	public Socket socket; //socket通信对象
	public ChatFrame chat; //聊天界面
	
	public P2PChatOut(Socket socket, ChatFrame chat, String chatUserName) {
		this.socket = socket;
		this.chat = chat;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), chatUserName);
		// 这表明对方那边充当服务器开设的线程，所以没有相关的ID信息
		OutputStream os;
		try {
			os = socket.getOutputStream(); //socket的输出流
			os.write(ServerLink.currentStudent.getBytes("US-ASCII"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public P2PChatOut(Socket socket, ChatFrame chat) {
		this.socket = socket;
		this.chat = chat;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), "default");
	}
	
	public void run() {
		OutputStream os;
		System.out.println("succeed to chat with "+this.chatUser.username);
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
