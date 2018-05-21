import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiP2PServer extends Thread{
	
	public void run() {
		ServerSocket ssocketWelcome;
		try {
			ssocketWelcome = new ServerSocket(7800);
			System.out.println("start for my P2P server engine.");
	        while(true){
	            Socket socketServer = ssocketWelcome.accept();
	            
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
