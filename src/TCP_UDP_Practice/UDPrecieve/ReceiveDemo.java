package TCP_UDP_Practice.UDPrecieve;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveDemo {
    public static void main(String[] args) throws IOException {
        //1. 创建接收端的Socket对象(DatagramSocket)
        DatagramSocket datagramSocket = new DatagramSocket(10086);

        //2. 创建数据包，用于接收数据
        byte[] bys = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bys, bys.length);

        //3.调用DatagramSocket 对象的方法接收数据
        datagramSocket.receive(datagramPacket);

        //4. 解析数据包并且显示在控制台
        System.out.println("数据是： " + new String(datagramPacket.getData(), 0, datagramPacket.getLength()));

        //5.关闭流
        datagramSocket.close();
    }
}
