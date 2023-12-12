package TCP_handShake;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class serverMsg {
    public static void main(String[] args) throws IOException {
        System.out.println("接收数据中:...");
        //创建接收端对象
        DatagramSocket datagramSocket = new DatagramSocket(9999);
        serverMsg serverMsg = new serverMsg();

        //创建数据包，用于接收数据
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);

        datagramSocket.receive(datagramPacket);
        String s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());

        //解析数据包并且输出显示
        System.out.println("数据为: " + s);

        //拆分字符串获取其中的SYN和Seq
        String[] strArr = s.split(" ", 2);
        //ISN(c)
        int ISN1 = Integer.parseInt(strArr[1]);
//        System.out.println(ISN1);


        try {
            System.out.println("====================");
            System.out.println("第二次握手:");
            System.out.println("正在发送SYN, Seq 和 ACK......");

            ConnectionMarks connectionMarks = new ConnectionMarks();
            //第二次握手，返回ACK = ISN + 1;
            //生成自己的ISN(s)
            String Seq2 = String.valueOf(connectionMarks.getSeq());
            //ACK2中的ISN为第一次传过来的ISN(c)+1
            String ACK2 = String.valueOf(ISN1+ 1);
            //将ack标志位设为1
            connectionMarks.setACKMark(1);
            String SYN2 = connectionMarks.getSYN() + "/" + connectionMarks.getACKMark();

            //2. 准备数据，一定要转成字节数组
            String data2 = SYN2 + " " + Seq2 + " " + ACK2;

            //创建数据，并把数据打包
            byte[] datas2 = data2.getBytes();
            DatagramPacket datagramPacket2 = new DatagramPacket(datas2, 0,datas2.length, new InetSocketAddress("localhost",8888));

            //调用对象发送数据
            datagramSocket.send(datagramPacket2);


            System.out.println("====================");
            System.out.println("接收数据中:...");
            String s3 = serverMsg.receive(datagramSocket, 75000, 0);
            //解析数据包并且输出显示
            System.out.println("数据为: " + s3);

            //拆分字符串获取其中的SYN,Seq和ACK
            String[] strArr3 = s3.split(" ");

            //System.out.println(strArr[0]);
            //检验接收信息是否是满足需求的
            if (
                    Integer.parseInt(strArr3[0]) != 1
                    || Integer.parseInt(strArr3[1]) != Integer.parseInt(ACK2)
                    || (Integer.parseInt(strArr3[2]) - 1) != Integer.parseInt(Seq2)
            ){
                throw new WrongConnectionException("非本次连接");
            }

            System.out.println("通过校验，完成三次握手");


            //数据传输开始
            System.out.println("====================");
            System.out.println("数据发送...");
            String SeqD1 = String.valueOf(connectionMarks.getSeq());
            String ACKD1 = String.valueOf(Integer.parseInt(strArr3[1]) + 1);
            String dataMsg = SeqD1 + " " + "我是马尔咖里斯，我是不朽的！" + " " + ACKD1;
            byte[] datasD1 = dataMsg.getBytes();
            DatagramPacket datagramPacketD1 = new DatagramPacket(datasD1, 0,datasD1.length, new InetSocketAddress("localhost",8888));

            int maxRetries = 3; // 最大重试次数
            int retryCount = 0; // 当前重试次数
            boolean success = false; // 是否成功标志

            while (!success && retryCount < maxRetries) {
                try {
                    // 设置超时时间
                    datagramSocket.setSoTimeout(50000);

                    // 发送数据
                    datagramSocket.send(datagramPacket);

                    // 接收响应
                    byte[] buffer = new byte[1024];
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    datagramSocket.receive(responsePacket);

                    // 处理响应
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    System.out.println("接收到响应：" + response);

                    // 设置成功标志
                    success = true;
                } catch (java.net.SocketTimeoutException e) {
                    // 超时后重新发送数据
                    retryCount++;
                    System.out.println("超时，进行第 " + retryCount + " 次重试");
                } catch (IOException e) {
                    // 处理其他异常
                    e.printStackTrace();
                }
            }

            if (!success) {
                System.out.println("重试次数超过最大限制，操作失败");
            }




            //四次握手
            //第一次
            System.out.println("====================");
            String receiveB1 = serverMsg.receive(datagramSocket, 0, 0);
            System.out.println("接收到的数据段为:" + receiveB1);

            String[] s1 = receiveB1.split(" ");
            String[] splitS1 = s1[0].split("/");
            if (
                    !(splitS1[0].equals("2")
                            || splitS1[1].equals("1")
                            || s1[2].equals(String.valueOf(Integer.parseInt(SeqD1) + 1)))
            ){
                throw new WrongConnectionException("非本次连接");
            }
            //第二次
            System.out.println("====================");
            System.out.println("服务端 -> 客户端");
            System.out.println("数据发送...");
            String SeqB2 = s1[2];
            String ACKB2 = String.valueOf(Integer.parseInt(s1[1]) + 1);
            connectionMarks.setACKMark(1);
            String ackMarkB = String.valueOf(connectionMarks.getACKMark());
            String dataMsgB2 = ackMarkB+ " " + SeqB2 + " " + ACKB2;
            byte[] datasB2 = dataMsgB2.getBytes();
            DatagramPacket datagramPacketB2 = new DatagramPacket(datasB2, 0,datasB2.length, new InetSocketAddress("localhost",8888));

            //调用对象发送数据
            datagramSocket.send(datagramPacketB2);

            System.out.println("====================");
            System.out.println("服务端 -> 客户端");
            System.out.println("数据发送...");

            String SeqB3 = SeqB2;
            String FinMark = splitS1[0];
            String ACKB3 = ACKB2;
            String dataMsgB3 = FinMark + "/" + ackMarkB+ " " + SeqB3 + " " + ACKB3;
            byte[] datasB3 = dataMsgB3.getBytes();
            DatagramPacket datagramPacketB3 = new DatagramPacket(datasB3, 0,datasB3.length, new InetSocketAddress("localhost",8888));

            //调用对象发送数据
            datagramSocket.send(datagramPacketB3);

            System.out.println("====================");
            String receiveB4 = serverMsg.receive(datagramSocket, 0, 0);
            System.out.println("接收到的数据段为:" + receiveB4);

            //关闭流
            datagramSocket.close();
        } catch (RuntimeException e) {
            throw new RuntimeException("超时错误，请求连接失败");
        }
        catch (WrongConnectionException e) {
            System.out.println("异常提醒:" + e.getMessage());
        }
    }
    //接收数据
    public String receive(DatagramSocket datagramSocket, int time1, int time2) throws IOException {
        //设置超时时间
        datagramSocket.setSoTimeout(time1);
        //创建数据包，用于接收数据
        byte[] bytes3 = new byte[1024];
        DatagramPacket datagramPacket3 = new DatagramPacket(bytes3, bytes3.length);

        datagramSocket.receive(datagramPacket3);
        //停止计时器
        datagramSocket.setSoTimeout(time2);
        String s3 = new String(datagramPacket3.getData(), 0, datagramPacket3.getLength());
        return s3;
    }
}
