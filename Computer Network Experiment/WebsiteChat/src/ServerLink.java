import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
	
	public static String receiveFilePath = "D:\\"; //设置的默认接收文件路径
	
	public static Map<String,ArrayList<String>> cacheContents = new HashMap<String,ArrayList<String>>(); //缓存还未发送内容的哈希表
	public static Map<String,ChatFrame> frames = new HashMap<String,ChatFrame>(); //好友和对话框的对应关系
	
	public static ArrayList<Socket> sockets = new ArrayList<Socket>(); //记录所有的连接，便于注销时全部关闭
	
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
	 * 获取字符串中有用的部分
	 * 
	 * @param content
	 * @return
	 */
	public static String getUsefulContent(String content) {
		int lastIdx = content.length()-1; //最后一个有实际意义的字符
		for (; lastIdx >= 0; lastIdx--) {
			if (content.charAt(lastIdx) != '\0') {
				break;
			}
		}
		return content.substring(0, lastIdx+1);
	}
	
	/**
	 * 判断文件路径是否合法
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistFile(String path) {		 
        if (null == path || "".equals(path.trim())) {
            return false;
        }
        File targetFile = new File(path);
        return targetFile.exists();
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
			System.out.println(queryResponse);
			if ((queryResponse.charAt(0) < '0' || queryResponse.charAt(0) > '9')
					&& (queryResponse.charAt(0) != '.' )) {
				if (queryResponse.charAt(0) == 'n') {
					System.err.println("Your friend isn't online!");
				} else {
					System.err.println(ServerLink.getUsefulContent(queryResponse));
				}
			}		
			return ServerLink.getUsefulContent(queryResponse);
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
	public boolean logout(String logoutCommand) {
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
			if (logoutResponse.substring(0,3).equals("loo")) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
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
        //passField.setText("net2018"); //为方便测试，直接输好密码
        
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
        JButton fileButton = new JButton("更换路径");
        fileButton.setFont(new Font("黑体", Font.PLAIN, 15));
        JButton logoutButton = new JButton("注销");
        logoutButton.setFont(new Font("黑体", Font.PLAIN, 15));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(queryButton);
        buttonPanel.add(chatButton);
        buttonPanel.add(fileButton);
        buttonPanel.add(logoutButton);
        
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
        			if (!ServerLink.cacheContents.containsKey(IP)) { //查看该用户是否已建立连接
        				JOptionPane.showMessageDialog(f,"Your friend is online, now send the chat request.");  			
            			try {
    						Socket chatSocket = new Socket(IP, MultiP2PServer.P2PPort); //新建一个与好友之间的socket连接对象
    						System.out.println("link server address:"+chatSocket.getInetAddress());
    						ChatFrame chat = new ChatFrame(IP);
    						chat.setChatTitle(friendId);
    						new P2PChatIn(chatSocket, chat, friendId).start();
    						new P2PChatOut(chatSocket, chat, friendId).start();
    						ServerLink.cacheContents.put(IP, new ArrayList<String>());
    						ServerLink.frames.put(IP, chat); //建立好友和聊天界面的对应关系
    						ServerLink.sockets.add(chatSocket); //将该套接字加入到列表中
    					} catch (UnknownHostException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					} catch (IOException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}         		
        			} else {
        				JOptionPane.showMessageDialog(f,"Your friend is online, continue to chat.");
        				ServerLink.frames.get(IP).setVisible(true); //将该界面设为可见
        			}       				
        		} else if (IP.equals("n")) {
        			JOptionPane.showMessageDialog(f,"Sorry, but your friend isn't online.");
        		} else {
        			JOptionPane.showMessageDialog(f,"Sorry, but "+IP);
        		}
            }
        });
        
        fileButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser fileChooser = new JFileChooser(ServerLink.receiveFilePath); //选择文件夹对话框 
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 

                int returnVal = fileChooser.showOpenDialog(fileChooser); 

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	String filePath= fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的
                	if (ServerLink.isExistFile(filePath)) { //判断文件路径是否合法
                		ServerLink.receiveFilePath = filePath+"\\";
                		System.out.println("succeed to change file path:"+filePath);
                	} else {
                		JOptionPane.showMessageDialog(f,"The file path is invalid, please rechoose.");
                	}
                }
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			for (Entry<String,ChatFrame> item: ServerLink.frames.entrySet()) {
        				item.getValue().dispose(); //清空所有的JFrame资源
        			}
        			for (int i = 0; i < ServerLink.sockets.size(); i++) { //注销时关闭所有的连接
            			if (ServerLink.sockets.get(i).isConnected()) {
    						ServerLink.sockets.get(i).close();
            			}
            		}
        			ServerLink.sockets.clear();
        			ServerLink.cacheContents.clear();
        			ServerLink.frames.clear();
        			String logoutCommand = "logout"+ServerLink.currentStudent; //发送登出命令
        			link.logout(logoutCommand);
        			link.mainFrame.setVisible(false);
        			link.welcomeFrame.setVisible(true); //打开欢迎界面
        		} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		try {
    				ServerLink.this.socket = new Socket(ServerLink.serverIP, ServerLink.serverPort); //新建一个socket连接对象
    				System.out.println("link server address:"+socket.getInetAddress());
    				ServerLink.this.os = socket.getOutputStream(); //socket的输出流
    				
    				InputStream is = socket.getInputStream(); //socket的输入流
    				InputStreamReader isr = new InputStreamReader(is);
    				ServerLink.this.br = new BufferedReader(isr);
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} 
            }
        });
        
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS)); //不同panel之间纵向排列
        f.getContentPane().add(welcomePanel);
        f.getContentPane().add(userPanel);
        f.getContentPane().add(buttonPanel);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(false); 
	}
	
	
	public static void main(String[] args) {
		if (args.length >= 1) {
			ServerLink.serverIP = args[0]; //设置连接服务器的IP地址
		}
		
		if (args.length >= 2) {
			ServerLink.serverPort = Integer.valueOf(args[1]); //设置连接的端口号
		}
		
		ServerLink link = ServerLink.getInstance();
		new MultiP2PServer().start();
		new MultiP2PFileServer().start();
		
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
