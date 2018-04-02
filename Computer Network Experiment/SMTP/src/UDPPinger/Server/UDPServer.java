package UDPPinger.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
	public static void main(String args[]) throws Exception{
		//设置UDP socket的端口号9876
		DatagramSocket serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024]; //缓存服务端接收到的数据
		byte[] sendData = new byte[1024]; //缓存服务器发送的数据
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			if(sentence != null && !sentence.equals("")){
				System.out.println(sentence);
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAddress,port);
			serverSocket.send(sendPacket);
		}
	}
}
