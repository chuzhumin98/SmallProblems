import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ServerLink {
	public static String serverIP = "166.111.140.84"; //服务器的IP地址
	//public static String serverIP = "166.111.140.52"; //服务器的IP地址
	//public static String serverIP = "localhost"; //服务器的IP地址
	public static int serverPort = 8000; //服务器端的端口号
	public static String studentID = "2015012158";
	
	public static Map<String,ArrayList<String>> cacheContents = new HashMap<String,ArrayList<String>>(); //缓存还未发送内容的哈希表
	
	public static ServerLink link; //单子模式对象
	
	public Socket socket;
	public OutputStream os;
	public BufferedReader br;
	
	/**
	 * 单子模式下实际的获取对象的语句
	 * 
	 * @return
	 */
	public static ServerLink getInstance() {
		if (link == null) {
			link = new ServerLink();
		}
		return link;
	}
	
	public ServerLink() {
		try {
			this.socket = new Socket(ServerLink.serverIP, ServerLink.serverPort); //新建一个socket连接对象
			System.out.println("link server address:"+socket.getInetAddress());
			this.os = socket.getOutputStream(); //socket的输出流
			
			InputStream is = socket.getInputStream(); //socket的输入流
			InputStreamReader isr = new InputStreamReader(is);
			this.br = new BufferedReader(isr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	/**
	 * 用户登录系统
	 * 
	 * @param loginCommand
	 * @return
	 */
	boolean login(String loginCommand) {
		try {
			os.write(loginCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send login request!");
			
			char[] responseBuffer = new char[20];
			int flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve login message from server!");
			}
			String loginResponse = String.valueOf(responseBuffer);
			System.out.println(loginResponse);
			if (!loginResponse.substring(0,3).equals("lol")) {
				System.err.println("invalid login command");			
				return false;
			} 
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 查询好友IP，如果找不到则返回null
	 * 
	 * @param queryCommand
	 * @return
	 */
	public String queryUser(String queryCommand) {		
		try {
			os.write(queryCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send query request!");
			
			char[] responseBuffer = new char [20];
			int flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve query message from server!");
			}
			String queryResponse = String.valueOf(responseBuffer);
			if (queryResponse.substring(0,1).equals("n")) {
				System.err.println("the query user isn't online");
				return null;
			}
			System.out.println(queryResponse);
			return queryResponse;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * 用户登出系统
	 * 
	 * @param logoutCommand
	 */
	public void logout(String logoutCommand) {
		try {
			os.write(logoutCommand.getBytes("US-ASCII"));
			System.out.println("succeed to send logout request!");
			
			char[] responseBuffer = new char [20];
			int flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve logout message from server!");
			}
			String logoutResponse = String.valueOf(responseBuffer);
			System.out.println(logoutResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		ServerLink link = ServerLink.getInstance();
		
		JFrame f = new JFrame();
        f.setTitle("Welcome Frame");
        f.setResizable(false);
        f.setBounds(100,50,300,300);
        
        //欢迎部分
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new FlowLayout());       
        JLabel welcomeLabel = new JLabel("登录界面");
        welcomeLabel.setForeground(Color.BLUE);
        welcomeLabel.setFont(new Font("黑体", Font.BOLD, 30));
        welcomePanel.add(welcomeLabel);
        
        // 账号部分
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout());       
        JLabel userLabel = new JLabel("登录账号:");
        JTextField userJtf = new JTextField(10);
        userLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userPanel.add(userLabel);
        userPanel.add(userJtf);

        // 密码部分
        JPanel passPanel = new JPanel();
        JLabel passLabel = new JLabel("登录密码:");
        passLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        JPasswordField passField = new JPasswordField(10);
        passPanel.setLayout(new FlowLayout());
        passPanel.add(passLabel);
        passPanel.add(passField);
        
        //登录按钮
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("黑体", Font.PLAIN, 15));
        loginButton.setSize(50,50);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginButton);
        
        
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //不同panel之间纵向排列
        f.getContentPane().add(welcomePanel);
        f.getContentPane().add(userPanel);
        f.getContentPane().add(passPanel);
        f.getContentPane().add(buttonPanel);
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);        

		
		//登入
		String loginCommand = ServerLink.studentID+"_net2018";
		link.login(loginCommand);
		//查询好友
		String queryCommand = "q2015012158";
		link.queryUser(queryCommand);
		
		//登出
		String logoutCommand = "logout"+ServerLink.studentID;
		link.logout(logoutCommand);
	}
}
