import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class P2PFileOut extends Thread {
	public Socket socket; //socket通信对象
	public ChatFrame chat; //聊天界面
	public String outPath; //输出文件地址
	public String IP; //IP地址
	
	public P2PFileOut(Socket socket, ChatFrame chat, String outPath) {
		this.socket = socket;
		this.chat = chat;
		this.outPath = outPath;
		this.IP = this.socket.getInetAddress().getHostAddress();
	}
	
	public void run() {
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {  
            File file=new File(this.outPath);  
            dos=new DataOutputStream(this.socket.getOutputStream());  
            dis=new DataInputStream(new BufferedInputStream(new FileInputStream(this.outPath)));  
            
            int buffferSize=1024;  
            byte[] bufArray=new byte[buffferSize];  
            dos.writeUTF(file.getName());   
            dos.flush();   
            dos.writeLong((long) file.length());   
            dos.flush();   
            while (true) {   
                int read = 0;   
                if (dis!= null) {   
                  read = dis.read(bufArray);   
                }   
  
                if (read == -1) {   
                  break;   
                }   
                dos.write(bufArray, 0, read);   
              }   
              dos.flush();  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } finally {   
              // 关闭所有连接   
              try {   
            	  if (dos != null)   
            		  dos.close();   
            	  if (dis != null)   
                      dis.close();  
            	  if (socket != null)   
                      socket.close(); 
              } catch (IOException e) {   
            	  e.printStackTrace();
              }   

        }   
	}
}
