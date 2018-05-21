import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatMultiServer {
	public static int clientNum = 0; //连接用户的个数
	
	public static void main(String[] args) {
		ServerSocket ssocketWelcome;
		try {
			ssocketWelcome = new ServerSocket(8000);
			System.out.println("start for server engine.");
	        while(true){
	            Socket socketServer = ssocketWelcome.accept();
	            ChatMultiServer.clientNum++; //增加了一个用户
	            ChatServerThread thread = new ChatServerThread(socketServer);
	            thread.start();
	            
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
}
