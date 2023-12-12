package TCP_UDP_Practice.UDPrecieve;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        System.out.println("接收方接收中。。。");
        DatagramSocket datagramSocket = new DatagramSocket(9999);
        byte[] container = new byte[1024 * 60];
        DatagramPacket packet = new DatagramPacket(container, 0, container.length);
        datagramSocket.receive(packet);
        System.out.println(new String(packet.getData(), 0, packet.getLength()));
        datagramSocket.close();
    }
}
