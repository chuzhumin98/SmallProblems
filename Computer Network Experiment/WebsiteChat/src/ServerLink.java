import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ServerLink {
	//public static String serverIP = "166.111.140.84"; //服务器的IP地址
	public static String serverIP = "166.111.140.52"; //服务器的IP地址
	//public static String serverIP = "localhost"; //服务器的IP地址
	public static int serverPort = 8000; //服务器端的端口号
	public static String studentID = "2015012158";
	
	public static Map<String,ArrayList<String>> cacheContents = new HashMap<String,ArrayList<String>>(); //缓存还未发送内容的哈希表
	public static Map<String,JFrame> chatFrames = new HashMap<String,JFrame>(); //各IP所对应的界面
	
	public static ServerLink link; //单子模式对象
	
	public static String currentStudent; //目前登录的学生的ID
	public Socket socket;
	public OutputStream os;
	public BufferedReader br;
	public JFrame welcomeFrame = new JFrame(); //欢迎界面
	public JFrame mainFrame = new JFrame(); //主界面
	
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
			
			char[] responseBuffer = new char[100];
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
			
			char[] responseBuffer = new char [100];
			int flag = br.read(responseBuffer);
			if (flag == -1) {
				System.err.println("cannot recieve query message from server!");
			}
			String queryResponse = String.valueOf(responseBuffer);
			if ((queryResponse.charAt(0) < '0' || queryResponse.charAt(0) > '9')
					&& (queryResponse.charAt(0) != '.' )) {
				System.err.println("the query user isn't online");
			}
			System.out.println(queryResponse);
			int lastIdx = 99; //最后一个有实际意义的字符
			for (; lastIdx >= 0; lastIdx--) {
				if (queryResponse.charAt(lastIdx) != '\0') {
					break;
				}
			}
			return queryResponse.substring(0, lastIdx+1);
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
			
			char[] responseBuffer = new char [100];
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
	
	void setWelcomeFrame(JFrame f) {
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
        passField.setText("net2018"); //为方便测试，直接输好密码
        
        //登录按钮
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("黑体", Font.PLAIN, 15));
        loginButton.setSize(50,50);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginButton);
        
        loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//登入
        		String userId = userJtf.getText();
        		String loginCommand = userId+"_"+passField.getText();
        		System.out.println("send command:"+loginCommand);
        		boolean status = link.login(loginCommand);
        		if (status) {
        			ServerLink.currentStudent = userId; //得到目前登录的学号，为后面做准备
        			f.setVisible(false);
        			link.mainFrame.setVisible(true);
        		}
            }
        });
        
        
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //不同panel之间纵向排列
        f.getContentPane().add(welcomePanel);
        f.getContentPane().add(userPanel);
        f.getContentPane().add(passPanel);
        f.getContentPane().add(buttonPanel);
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);        
	}
	
	
	/**
	 * 设置主界面，即总功能的大厅
	 * 
	 * @param f
	 */
	public void setMainFrame(JFrame f) {
		f.setTitle("Main Frame");
        f.setResizable(false);
        f.setBounds(100,50,400,200);
        
        //欢迎部分
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new FlowLayout());       
        JLabel welcomeLabel = new JLabel("大厅界面");
        welcomeLabel.setForeground(Color.BLUE);
        welcomeLabel.setFont(new Font("黑体", Font.BOLD, 30));
        welcomePanel.add(welcomeLabel);
        
        //好友账号部分
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout());       
        JLabel userLabel = new JLabel("好友ID:");
        JTextField userField = new JTextField(10);
        userLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userPanel.add(userLabel);
        userPanel.add(userField);
        
        //查询与聊天按钮
        JPanel buttonPanel = new JPanel();
        JButton queryButton = new JButton("查询");
        queryButton.setFont(new Font("黑体", Font.PLAIN, 15));
        JButton chatButton = new JButton("聊天");
        chatButton.setFont(new Font("黑体", Font.PLAIN, 15));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(queryButton);
        buttonPanel.add(chatButton);
        
        queryButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//查询好友是否在线
        		String friendId = userField.getText();
        		String queryCommand = "q"+friendId;
        		System.out.println("send command:"+queryCommand);
        		String IP = link.queryUser(queryCommand);
        		//System.out.println(IP.length());
        		if ((IP.charAt(0) >= '0' && IP.charAt(0) <= '9')
    					|| (IP.charAt(0) == '.' )) {
        			JOptionPane.showMessageDialog(f,"Your friend is online, his IP address \nis "+IP+".");
        		} else if (IP.equals("n")) {
        			JOptionPane.showMessageDialog(f,"Sorry, but your friend isn't online.");
        		} else {
        			JOptionPane.showMessageDialog(f,"Sorry, but "+IP);
        		}
            }
        });
        
        chatButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//查询好友是否在线
        		String friendId = userField.getText();
        		String queryCommand = "q"+friendId;
        		System.out.println("send command:"+queryCommand);
        		String IP = link.queryUser(queryCommand);
        		//System.out.println(IP.length());
        		if ((IP.charAt(0) >= '0' && IP.charAt(0) <= '9')
    					|| (IP.charAt(0) == '.' )) {
        			JOptionPane.showMessageDialog(f,"Your friend is online, now send the chat request.");     
        			try {
						Socket chatSocket = new Socket(IP, 7800); //新建一个与好友之间的socket连接对象
						System.out.println("link server address:"+chatSocket.getInetAddress());
						
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}         			
        		} else if (IP.equals("n")) {
        			JOptionPane.showMessageDialog(f,"Sorry, but your friend isn't online.");
        		} else {
        			JOptionPane.showMessageDialog(f,"Sorry, but "+IP);
        		}
            }
        });
        
        
        
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //不同panel之间纵向排列
        f.getContentPane().add(welcomePanel);
        f.getContentPane().add(userPanel);
        f.getContentPane().add(buttonPanel);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true); 
	}
	
	/**
	 * 建立聊天界面
	 * 
	 * @return
	 */
	public JFrame buildChatFrame() {
		return null;
	}
	
	public static void main(String[] args) {
		ServerLink link = ServerLink.getInstance();
		new MultiP2PServer().start();
		
		link.setMainFrame(link.mainFrame); //设置主界面
        link.setWelcomeFrame(link.welcomeFrame); //设置欢迎界面（登录）
        
        

		/*
		//登入
		String loginCommand = ServerLink.studentID+"_net2018";
		link.login(loginCommand);
		//查询好友
		String queryCommand = "q2015012158";
		link.queryUser(queryCommand);
		
		//登出
		String logoutCommand = "logout"+ServerLink.studentID;
		link.logout(logoutCommand);
		*/
	}
}
