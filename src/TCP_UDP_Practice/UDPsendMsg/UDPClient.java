package TCP_UDP_Practice.UDPsendMsg;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        System.out.println("发送启动中。。。");
        
        //1. 使用 DatagramSocket(8888)
        DatagramSocket datagramSocket = new DatagramSocket(8888);

        //2. 准备数据，一定要转成字节数组
        String data = "hello java";
        //创建数据，并把数据打包
        byte[] datas = "hello java".getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(datas, 0,datas.length, new InetSocketAddress("localhost",9999));

        //调用对象发送数据
        datagramSocket.send(datagramPacket);

        //关闭流
        datagramSocket.close();
    }
}
