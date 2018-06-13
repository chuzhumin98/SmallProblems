import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	
	public ChatFrame() {
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
	
	public static void main(String[] args) {
		ChatFrame frame1 = new ChatFrame();
		frame1.appendMessage("Hello, world!");
	}
}
