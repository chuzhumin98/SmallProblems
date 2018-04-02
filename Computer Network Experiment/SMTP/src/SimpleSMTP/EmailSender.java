package SimpleSMTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class EmailSender {
	public static void main(String[] args) throws Exception {
		Date dDate = new Date();
		DateFormat dFormat = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,
				Locale.US);
		String command = null;

		/** 与邮件服务器建立TCP连接. */
		// TODO: 1.在""中填入我们的smtp服务器和正确端口，助教老师的服务器地址166.111.74.90，端口是25
		// e.g. Socket socket = new Socket("mails.163.com",25);
		Socket socket = new Socket("mails.tsinghua.edu.cn", 25);

		/** 创建BufferedReader每次读入一行信息. */
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		/** 读入系统的欢迎信息. */
		String response = br.readLine();
		System.out.println(response);
		// TODO: 2.把code改为合适的代码
		String[] splits = response.split(" ");
		int code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 取得socket输出流的引用. */
		OutputStream os = socket.getOutputStream();

		/** 发送 HELO 命令并取得服务器响应. 
		 *	填入所需的命命, 在以下的"\r\n"前写入所需的命令 
		 *	e.g.	command = "Helo x\r\n";
		 *	其中\r\n为回车符,每个命今都必需以它们结尾. */
		// TODO: 3.填入命令
		command = "HELO mails.tsinghua.edu.cn\r\n";		
		System.out.print(command);
		os.write(command.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
		// TODO: 4.把code改为合适的代码
		splits = response.split(" ");
		code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 发送 MAIL FROM 命令. */
		// TODO: 5.填入命令
		command = "MAIL FROM: <chuzhumin98@gmail.com>\r\n";
		String mailFrom = command; //记录发送方信息
		System.out.print(command);
		os.write(command.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
		// TODO: 6.把code改为合适的代码
		splits = response.split(" ");
		code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 发送 RCPT TO 命令. */
		// TODO: 7.填入命令
		command = "RCPT TO: <chuzm15@mails.tsinghua.edu.cn>\r\n";
		String rcptTo = command;
		System.out.print(command);
		os.write(command.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
		// TODO: 8.把code改为合适的代码
		splits = response.split(" ");
		code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 发送 DATA 命令. */
		// TODO: 9.填入命令
		command = "DATA\r\n";
		System.out.print(command);
		os.write(command.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
		// TODO: 10.把code改为合适的代码
		splits = response.split(" ");
		code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 自动写入当前的日期 */
		String date = "DATE: " + dFormat.format(dDate) + "\r\n";
		System.out.print(date);
		os.write(date.getBytes("US-ASCII"));
		String str = "";
		// TODO: 11.把"x@x.x"改为邮件中显示的发件人地址
		String[] part = mailFrom.split(">");
		String[] from = part[0].split("<");
		str = "From:" + from[1] + "\r\n";
		System.out.print(str);
		os.write(str.getBytes("US-ASCII"));
		// TODO: 12.把"x@x.x"改为邮件中显示的收件人地址
		part = rcptTo.split(">");
		String[] to = part[0].split("<");
		str = "To:" + to[1] + "\r\n";
		System.out.print(str);
		os.write(str.getBytes("US-ASCII"));

		/** 发送邮件內容. */
		// TODO: 13.在"x"中填入Subject内容.
		str = "SUBJECT:" + "Test Simple STMP" + "\r\n\r\n";
		System.out.print(str);
		os.write(str.getBytes("US-ASCII"));
		// TODO: 14.在"x"中填入邮件正文内容.
		str = "This is an interesting try to confirm that my simple SMTP "
				+ "program is correctly coded. See you!" + "\r\n";
		System.out.print(str);
		os.write(str.getBytes("US-ASCII"));

		/** 以.作为邮件内容的结束符 */
		str = ".\r\n";
		System.out.print(str);
		os.write(str.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
		// TODO: 15.把code改为合适的代码
		splits = response.split(" ");
		code = Integer.valueOf(splits[0]);	//把-1改为合适的代码
		if (!response.startsWith(Integer.toString(code))) {
			throw new Exception(code + " reply not received from server.");
		}

		/** 发送 QUIT 命令. */
		//TODO:	16.填入命令
		command = "QUIT\r\n";
		System.out.print(command);
		os.write(command.getBytes("US-ASCII"));
		response = br.readLine();
		System.out.println(response);
	}
}
