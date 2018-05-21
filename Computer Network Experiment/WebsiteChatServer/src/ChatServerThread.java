import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ChatServerThread extends Thread{
	public Socket socket;
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;

	}
	
	public void run() {
		InputStream is;
		System.out.println("IP:"+this.socket.getInetAddress());
		System.out.println("Port:"+this.socket.getPort());
		try {
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			OutputStream os = socket.getOutputStream(); //socket的输出流
	
			while (true) {
				String command = br.readLine();
				System.out.println(command);
				if (command.length() >= 18) {
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //socket的输入流
		
		
	}
}
