import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class P2PFileIn extends Thread {
	public Socket socket; //socket通信对象
	public ChatFrame chat; //对应的聊天界面
	public String savePath; //保存文件的地址
	public String IP; //对方的IP地址
	
	public P2PFileIn(Socket socket, ChatFrame chat, String savePath) {
		this.socket = socket;
		this.chat = chat;
		this.savePath = savePath;
		this.IP = this.socket.getInetAddress().getHostAddress();
	}
	
	public void run() { 
        DataInputStream dis=null;  
        try {   
        	dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));   
        } catch (IOException e1) {   
            e1.printStackTrace();   
        }   
        int bufferSize = 1024;   
        // 缓冲区   
        byte[] buf = new byte[bufferSize];   
        int passedlen = 0;   
        long len = 0;   
        // 获取文件名称   
        try{  
        	String filename = dis.readUTF();
        	this.chat.appendMessage("start to receive file "+filename);
        	savePath += filename; //追加上文件名
        	System.out.println("start to save file "+savePath);
        	DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream( 
        					new FileOutputStream(savePath)));   
        	len = dis.readLong();   
        	System.out.println("文件的长度为:" + len + " B");   
        	System.out.println("开始接收文件!");   
        	while (true) {   
        		int read = 0;   
        		if (dis!= null) {   
        			read = dis.read(buf);   
        		}
        		passedlen += read;   
        		if (read == -1) {   
        			break;   
        		}   
        		System.out.println("文件接收了" + (passedlen * 100 / len) + "%");   
        		fileOut.write(buf, 0, read);   
        	}   
        	System.out.println("接收完成，文件存为" + savePath);   
        	fileOut.close();   
        	this.chat.appendMessage("end to receive file "+filename);
        	ArrayList<String> contents = ServerLink.cacheContents.get(this.IP);
        	String message = "end to send file "+filename;
        	contents.add(message);
        	ServerLink.cacheContents.put(IP, contents);
        } catch (Exception e) {   
        	e.printStackTrace();   
        	return;   
        }   
	}
}
