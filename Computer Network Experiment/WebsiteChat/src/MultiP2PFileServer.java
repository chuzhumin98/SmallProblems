import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiP2PFileServer extends Thread {
	public static final int P2PPort = 4567; //P2P服务端所监听的端口号
	
	public void run() {
		ServerSocket ssocketWelcome;
		try {
			ssocketWelcome = new ServerSocket(MultiP2PFileServer.P2PPort);
			System.out.println("start for my P2P file server engine.");
	        while(true){
	            Socket socketServer = ssocketWelcome.accept();
	            ChatFrame chat = ServerLink.frames.get(socketServer.getInetAddress().getHostAddress());
				new P2PFileIn(socketServer, chat, ServerLink.receiveFilePath).start(); //开启数据传输线程
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
