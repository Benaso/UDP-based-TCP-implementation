package TCP_UDP_Practice.TCPrecieve;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10005);

        Socket accept = serverSocket.accept();

        //获取输入流
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        String s = new String(bytes, 0, read);
        System.out.println("数据是：" + s);

        //关闭流
        serverSocket.close();
    }
}
