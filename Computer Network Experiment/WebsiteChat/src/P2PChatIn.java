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
			this.chatUser.username = userName;
			this.chat.setChatTitle(userName);
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
				if (!this.socket.isConnected()) {
					this.socket.shutdownInput();
					this.socket.shutdownOutput();
					this.socket.close();
					break;
				}
				char[] responseBuffer = new char[100];
				br.read(responseBuffer);
				String content = ServerLink.getUsefulContent(String.valueOf(responseBuffer));
				System.out.println(this.chatUser.username+":"+content);
				this.chat.appendMessage(this.chatUser.username+":"+content);
				Thread.sleep(500);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//连接断开时，则清空相关的数据
		if (ServerLink.cacheContents.containsKey(this.chat.IP)) {
			ServerLink.cacheContents.remove(this.chat.IP);
		}
		if (ServerLink.frames.containsKey(this.chat.IP)) {
			ServerLink.frames.remove(this.chat.IP);
		}
		System.out.println("succeed to release input thread!");
	}
}
