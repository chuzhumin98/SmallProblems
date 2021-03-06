import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame {
	public JTextArea content;
	public JLabel chatTitle;
	public JTextField inputField;
	public JButton sendButton;
	public JButton fileButton;
	
	public String IP; //该JFrame所对应的好友
	
	public ChatFrame(String IP) {
		this.IP = IP;
		this.setTitle("Chat Frame");
		//this.setResizable(false);
        this.setBounds(100,50,800,600);
        
        //聊天头部分
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        this.chatTitle = new JLabel();
        this.chatTitle.setText("与好友聊天");
        this.chatTitle.setFont(new Font("黑体", Font.BOLD, 30));
        titlePanel.add(chatTitle);
        
        this.content = new JTextArea(28, 20); //聊天显示区域
        content.setText("");
        content.setLineWrap(true);//设置为自动换行,初始值为true
        content.setTabSize(4);//设置制表符的大小为8个字符,初始值为4个字符
        //content.setBackground(Color.white);//文本区背景
        content.setEnabled(false);
        content.setForeground(Color.BLACK);//字体颜色      
        content.setFont(new Font("宋体", Font.BOLD, 14));
        JScrollPane contentPanel = new JScrollPane(content);
        
        JPanel operationPanel = new JPanel();
        this.inputField = new JTextField(40);
        operationPanel.setLayout(new FlowLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        this.sendButton = new JButton("发送消息");
        this.fileButton = new JButton("发送文件");
        buttonPanel.add(sendButton);
        buttonPanel.add(fileButton);
        operationPanel.add(inputField);
        operationPanel.add(buttonPanel);            
        
        this.sendButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String content = inputField.getText();
        		if (content != null && content.length() >= 1) {
        			ArrayList<String> contents = ServerLink.cacheContents.get(IP);
        			contents.add(content);
        			ServerLink.cacheContents.put(IP, contents);
        		}
            }
        });
        
        this.fileButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		FileDialog filedialogSend = new FileDialog(ChatFrame.this, "选择要发送的文件", FileDialog.LOAD);  
                // 打开文件对话框适配器  
                filedialogSend.addWindowListener(new WindowAdapter() {  
                    public void windowClosing(WindowEvent e) {  
                        filedialogSend.setVisible(false);  
                    }  
                });  
                filedialogSend.setVisible(true);
                String fileopen = filedialogSend.getDirectory();// 返回文件对话框中显示的文件所属的目录  
                String filename = filedialogSend.getFile();// 返回当前文件对话框中显示的文件名的字符串表示
                if (fileopen != null && filename != null) {
                	System.out.println("start to send the file "+fileopen+filename+" to "+IP);
                	Socket fileSocket;
					try {
						fileSocket = new Socket(IP, MultiP2PFileServer.P2PPort); //新建一个与好友之间的socket连接对象
						System.out.println("link server address:"+fileSocket.getInetAddress());
						new P2PFileOut(fileSocket, ChatFrame.this, fileopen+filename).start(); //开启数据传输线程
						ChatFrame.this.appendMessage("start to send file "+filename);
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 			
                }
                //System.out.println(fileopen+" "+filename);
            }
        });
        
        this.addWindowListener(new WindowAdapter() {    	         	  
        	public void windowClosing(WindowEvent e) {  
        		super.windowClosing(e);  
        		//如果对方用户已关闭，则杀死这个界面
        		if (!ServerLink.cacheContents.containsKey(ChatFrame.this.IP)) {
        			System.out.println("kill the chat frame with "+ChatFrame.this.IP);
        			ChatFrame.this.dispose();
        		}
        	}  
        	  
        });  
        
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(titlePanel);
        this.getContentPane().add(contentPanel);
        this.getContentPane().add(operationPanel);
        
        this.setVisible(true); 
	}
	
	/**
	 * 在textarea中追加消息
	 * 
	 * @param message
	 */
	public void appendMessage(String message) {
		String msg = this.content.getText();
		this.content.setText(msg+message+"\n");
	}
	
	public void setChatTitle(String title) {
		this.setTitle(title);
		this.chatTitle.setText(title);
	}
	
	public static void main(String[] args) {
		ChatFrame frame1 = new ChatFrame("localhost");
		frame1.appendMessage("Hello, world!");
	}
}
