package TCP_UDP_Practice.TCPsendMsg;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 10005);
        //创建输入流对象，写入数据
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello tcp".getBytes());
        //关闭流
        socket.close();
    }
}
