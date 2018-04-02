package UDPPinger.Client;

/**
 * UDPClient.java -- Simple UDP client
 *
 * $Id: UDPClient.java,v 1.2 2003/10/14 14:25:30 kangasha Exp $
 *
 */

import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

public class UDPClient extends UDPPinger implements Runnable {
	/** Host to ping */
	String remoteHost;

	/** Port number of remote host */
	int remotePort;

	/** How many pings to send */
	static final int NUM_PINGS = 10;

	/** How many reply pings have we received */
	int numReplies = 0;

	/** Array for holding replies and RTTs */
	static boolean[] replies = new boolean[NUM_PINGS];

	static long[] rtt = new long[NUM_PINGS];

	/*
	 * Send our own pings at least once per second. If no replies received
	 * within 5 seconds, assume ping was lost.
	 */
	/** 1 second timeout for waiting replies */
	static final int TIMEOUT = 1000;

	/** 5 second timeout for collecting pings at the end */
	static final int REPLY_TIMEOUT = 5000;

	/** Create UDPClient object. */
	public UDPClient(String host, int port) {
		remoteHost = host;
		remotePort = port;
	}

	/**
	 * Main Function.
	 */
	public static void main(String args[]) {
		//TODO: 修改为期望Ping的机器正确的IP地址和端口,现在是本机设置.改端口时同时要改UDPServer的设置.
		String host = "localhost";
		int port = 9876;

		System.out.println("Contacting host " + host + " at port " + port);

		UDPClient client = new UDPClient(host, port);
		client.run();	//运行程序
	}
	
	/** Main code for pinger client thread. */
	public void run() {
		/* Create socket. We do not care which local port we use. */
		createSocket();
		try {
			socket.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
			System.out.println("Error setting timeout TIMEOUT: " + e);
		}

		for (int i = 0; i < NUM_PINGS; i++) {
			/*
			 * Message we want to send. Add space at the end for easy parsing of
			 * replies.
			 */
			Date now = new Date();
			String message = "PING " + i + " " + now.getTime() + " this is the end of message ";
			replies[i] = false;
			rtt[i] = 1000000;
			PingMessage ping = null;

			/* Send ping to recipient */
			try {
				//TODO: 1.PingMessage对象包含了什么信息?
				//包括需要连接的服务器的IP地址，端口号，需要发送或接收的信息
				ping = new PingMessage(InetAddress.getByName(remoteHost),
						remotePort, message);
			} catch (UnknownHostException e) {
				System.out.println("Cannot find host: " + e);
			}
			sendPing(ping);

			/* Read reply */
			try {
				//TODO: 2.这里取得的PingMessage reply对象与上面发送的ping对象内容是否一样?
				//内容完全一样，因为在server端，其就是将收到的内容打印到控制台，然后将其转存成一个新的对象传回来
				PingMessage reply = receivePing();
				//TODO:	3.handleReply的作用?是以致它所改变的变量值是什么?
				//作用是处理收到的数据，得到它所属的ping的编号和数据发送时的时间，然后根据时间差计算相应的RTT
				//它所改变的值有对应编号ping的RTT(rtt[pingNumber])、该次是否获得响应记为true(replies[pingNumber])、
				//获得响应总次数加一(numReplies)
				handleReply(reply.getContents());	
			} catch (SocketTimeoutException e) {
				/*
				 * Reply did not arrive. Do nothing for now. Figure out lost
				 * pings later.
				 */
			}
		}
		/*
		 * We sent all our pings. Now check if there are still missing replies.
		 * Wait for a reply, if nothing comes, then assume it was lost. If a
		 * reply arrives, keep looking until nothing comes within a reasonable
		 * timeout.
		 */
		try {
			socket.setSoTimeout(REPLY_TIMEOUT);
		} catch (SocketException e) {
			System.out.println("Error setting timeout REPLY_TIMEOUT: " + e);
		}
		//TODO:	4.这个while循环的作用是什么,与第96行的receivePing调用有什么关系,既然上面都已调用了receivePing()
		//		为何这里要重新调用??
		//问题在于那个地方可能会触发SocketTimeoutException，即超时异常，在那种情况下，
		//只是说它在ping的时间间隔（即1s）内无法返回信息，不见得这个服务器一定连接不上，
		//因此，在这里又将时间长短增至5s,用来抓捕那些潜在的其他可能传来的信息
		//可以说，这里可以认为是对ping功能的一个扩充
		while (numReplies < NUM_PINGS) {
			try {
				PingMessage reply = receivePing();
				handleReply(reply.getContents());
			} catch (SocketTimeoutException e) {
				/* Nothing coming our way apparently. Exit loop. */
				numReplies = NUM_PINGS;
			}
		}
		/* Print statistics */
		for (int i = 0; i < NUM_PINGS; i++) {
			System.out.println("PING " + i + ": " + replies[i] + " RTT: "
					+ ((rtt[i] > 0) ? Long.toString(rtt[i]):"< 1")+ " ms");
		}

	}

	/**
	 * Handle the incoming ping message. For now, just count it as having been
	 * correctly received.
	 */
	private void handleReply(String reply) {
		String[] tmp = reply.split(" ");
		int pingNumber = Integer.parseInt(tmp[1]);
		long then = Long.parseLong(tmp[2]);
		replies[pingNumber] = true;
		/* Calculate RTT and store it in the rtt-array. */
		Date now = new Date();
		//TODO:	5. 请简要说明这里的rtt的计算过程.
		//这里RTT的计算过程，是在client端向server发送数据报时加上一个当时的时间t1,
		//接着server端收到数据，并将数据原样返回给client端，client端将收到的时间与现在的时间t2,
		//求一个差值，得到的t2-t1即为RTT
		rtt[pingNumber] = now.getTime() - then;	
		numReplies++;
	}
}
