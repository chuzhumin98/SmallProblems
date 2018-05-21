import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ChatServerThread extends Thread{
	public Socket socket;
	public String username; //用户名
	public String IP; //IP地址
	public int port; //端口号
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;

	}
	
	public void run() {
		InputStream is;
		this.IP = this.socket.getInetAddress().getHostAddress();
		this.port = this.socket.getPort();
		System.out.println("IP:"+this.IP);
		System.out.println("Port:"+this.port);
		try {
			is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			OutputStream os = socket.getOutputStream(); //socket的输出流
			/** 登录操作部分 */
			while (true) {
				boolean isAlreadyOnline = false; //判断该学号用户是不是已经在线
				char[] responseBuffer = new char[20];
				int flag = br.read(responseBuffer);
				if (flag == -1) {
					System.err.println("cannot recieve login message from client "+this.IP+"!");
				}
				String command = String.valueOf(responseBuffer);
				System.out.println("receive request:"+command);
				//判断是否满足登录的语法要求，如果满足则回复"lol"
				int offset = 18; //终止的index
				if (command.charAt(offset) == '\0' || command.charAt(offset) == '\r' || command.charAt(offset) == '\n') {
					this.username = command.substring(0, 10);
					boolean existUser = false; //判断这个学号是否合法
					for (int i = 0; i < ChatMultiServer.validIDs.size(); i++) {
						if (ChatMultiServer.validIDs.get(i).equals(this.username)) {
							existUser = true;
							break;
						}
					}
					System.out.println("usersize:"+ChatMultiServer.users.size());
					for (int i = 0; i < ChatMultiServer.users.size(); i++) {
						if (ChatMultiServer.users.get(i).username.equals(this.username)) {
							isAlreadyOnline = true;
							String loginFailResponse = "already login";
							os.write(loginFailResponse.getBytes("US-ASCII"));
							break;
						} 
					}
					String subCommand = command.substring(10, 18);
					if (existUser && !isAlreadyOnline && subCommand.equals("_net2018")) {
						UserInfo thisUser = new UserInfo(this.IP, this.port, this.username); //新建一个用户到用户表中
						ChatMultiServer.users.add(thisUser);
						String loginResponse = "lol";
						os.write(loginResponse.getBytes("US-ASCII"));
						System.out.println("succeed to send login response to "+this.username+"!");
						break;
					}
				}
				if (!isAlreadyOnline) {
					String loginFailResponse = "invalid login";
					os.write(loginFailResponse.getBytes("US-ASCII"));
				}
			}
			/** 查询和登出操作 */
			while (true) {
				boolean validOperation = false; //记录本轮操作是否合法
				char[] responseBuffer = new char[20];
				int flag = br.read(responseBuffer);
				if (flag == -1) {
					System.err.println("cannot recieve login message from client "+this.IP+"!");
				}
				String command = String.valueOf(responseBuffer);
				System.out.println("receive request:"+command);
				int offset = 11;
				if (command.charAt(offset) == '\0' || command.charAt(offset) == '\r' || command.charAt(offset) == '\n') {
					if (command.charAt(0) == 'q') {
						String queryUserName = command.substring(1, 11);
						System.out.println(this.username+" has queried for "+queryUserName);
						//找到查看好友是否在线
						String targetIP = "";
						int targetPort = -2;
						for (int i = 0; i < ChatMultiServer.validIDs.size(); i++) {
							if (ChatMultiServer.validIDs.get(i).equals(this.username)) {
								targetPort = -1; //如果这个查询的学号合法，则返回n，否则则返回非法输入
								break;
							}
						}
						for (UserInfo item: ChatMultiServer.users) {
							if (item.username.equals(queryUserName)) {
								targetIP = item.IP;
								targetPort = item.port;
							}
						}
						if (targetPort == -1) {
							validOperation = true;
							String response = "n";
							os.write(response.getBytes("US-ASCII"));
							System.out.println("succeed to send response "+response+" to "+this.username+"!");
						} else if (targetPort != -2) {
							validOperation = true;
							String response = targetIP;
							os.write(response.getBytes("US-ASCII"));
							System.out.println("succeed to send response "+response+" to "+this.username+"!");
						}
					}
				}
				offset = 16;
				if (command.charAt(offset) == '\0' || command.charAt(offset) == '\r' || command.charAt(offset) == '\n') {
					if (command.substring(0, 6).equals("logout") && command.substring(6, 16).equals(this.username)) {
						validOperation = true;
						String logoutResponse = "loo";
						os.write(logoutResponse.getBytes("US-ASCII"));
						System.out.println("succeed to send logout response to "+this.username+"!");
						break;
					}
				}
				if (!validOperation) {
					String loginResponse = "invalid operation";
					os.write(loginResponse.getBytes("US-ASCII"));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //socket的输入流	
		for (int i = ChatMultiServer.users.size()-1; i >= 0; i--) {
			UserInfo item = ChatMultiServer.users.get(i);
			if (item.IP.equals(this.IP) && item.port == this.port && item.username.equals(this.username)) {
				ChatMultiServer.users.remove(item);
				System.out.println("has remove for user "+this.username+" online");
			}
		}
	}
}
