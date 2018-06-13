import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiP2PServer extends Thread{
	
	public void run() {
		ServerSocket ssocketWelcome;
		try {
			ssocketWelcome = new ServerSocket(7800);
			System.out.println("start for my P2P server engine.");
	        while(true){
	            Socket socketServer = ssocketWelcome.accept();
	            String IP = socketServer.getInetAddress().getHostAddress();
	            ChatFrame chat = new ChatFrame(IP);
	            new P2PChatIn(socketServer, chat).start();
				new P2PChatOut(socketServer, chat).start();
				ServerLink.cacheContents.put(IP, new ArrayList<String>());
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
