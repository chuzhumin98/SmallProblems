import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class P2PChatIn extends Thread {
	public UserInfo chatUser; //聊天对象的信息
	public Socket socket; //socket通信对象
	public ChatFrame chat; //对应的聊天界面
	
	public P2PChatIn(Socket socket, ChatFrame chat, String chatUserName) {
		this.socket = socket;
		this.chat = chat;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), chatUserName);
	}
	
	public P2PChatIn(Socket socket, ChatFrame chat) {
		this.socket = socket;
		this.chat = chat;
		this.chatUser = new UserInfo(socket.getInetAddress().getHostAddress(), socket.getPort(), "default");
		InputStream is; //socket的输入流
		try {
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			char[] responseBuffer = new char[20];
			br.read(responseBuffer);
			String userName = ServerLink.getUsefulContent(String.valueOf(responseBuffer));
			System.out.println("get the chatting partner:"+userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
