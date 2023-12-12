package TCP_UDP_Practice.UDPsendMsg;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP发送数据：
 *  来自键盘键入，如果输入886则发送数据结束
 */
public class  sendDemo{
    public static void main(String[] args) throws IOException {
        //创建接收端Socket对象
        DatagramSocket datagramSocket = new DatagramSocket();

        //创建数据，并把数据打包
        byte[] bys = "hello java".getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bys, bys.length, InetAddress.getByName("127.0.0.1"), 10086);

        //调用对象发送数据
        datagramSocket.send(datagramPacket);

        //关闭流
        datagramSocket.close();
    }
}