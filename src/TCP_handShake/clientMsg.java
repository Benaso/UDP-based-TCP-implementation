package TCP_handShake;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 客户端，用于发送信息和握手请求
 * version 1
 */
//TODO 超时重传
public class clientMsg {
    public static void main(String[] args) throws IOException {

        DatagramSocket datagramSocket;
        ConnectionMarks connectionMarks;
        clientMsg clientMsg = new clientMsg();
        String[] strArr;

        try {
            System.out.println("第一次握手:");
            System.out.println("正在发送SYN和Seq......");

            //1. 使用 DatagramSocket(8888)
            datagramSocket = new DatagramSocket(8888);
            connectionMarks = new ConnectionMarks();
            String SYN = String.valueOf(connectionMarks.getSYN());
            //getSeq() 方法值等同于 getISN(),获取ISN(c)
            int ISN1 = connectionMarks.getSeq();
            String Seq = String.valueOf(ISN1);

            //2. 准备数据，一定要转成字节数组
            String data = SYN + " " + Seq;
            //创建数据，并把数据打包
            byte[] datas = data.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(datas, 0,datas.length, new InetSocketAddress("localhost",9999));
            //调用对象发送数据
            datagramSocket.send(datagramPacket);
            datagramSocket.setSoTimeout(75000);


            System.out.println("====================");
            System.out.println("接收数据中:...");

            /**
             * 在第二次握手中，客户端主要会检查两个方面的内容：
             * 检查ACK标志位：客户端需要确认服务端发送的确认信息（SYN-ACK）中的ACK标志位是否已设置。ACK标志位表示服务端确认收到了客户端的握手请求。
             * 检查确认号（ACK）：客户端需要检查服务端发送的确认信息中的确认号（ACK）是否正确。确认号应该是服务端发送的初始序列号加1，用于告知服务端它已经正确接收到服务端的数据。
             */
            //创建数据包，用于接收数据
            byte[] bytes = new byte[1024];
            DatagramPacket datagramPacket2 = new DatagramPacket(bytes, bytes.length);
            datagramSocket.receive(datagramPacket2);
            //停止计时
            datagramSocket.setSoTimeout(0);

            String s = new String(datagramPacket2.getData(), 0, datagramPacket2.getLength());

            //拆分字符串获取其中的SYN,Seq和ACK
            strArr = s.split(" ");
            String[] flag = strArr[0].split("/");
            //System.out.println(strArr[0]);
            //检验接收信息是否是满足需求的
            if (!(
                    Integer.parseInt(flag[1]) != 0
                    && Integer.parseInt(flag[0]) == 1
                    && Integer.parseInt(strArr[2]) == ISN1 + 1)
            ){
                //TODO 异常提醒，非本次连接，如何处理
                throw new RuntimeException("非本次连接");
            }
            System.out.println("通过校验");
            //解析数据包并且输出显示
            System.out.println("数据为: " + s);

            //第三次握手
            System.out.println("====================");
            System.out.println("第三次握手:");
            System.out.println("正在发送SYN, Seq 和 ACK......");
            connectionMarks.setACKMark(1);
            String ackMark = String.valueOf(connectionMarks.getACKMark());
            String Seq3 = strArr[2];
            String ACK3 = String.valueOf(Integer.parseInt(strArr[1]) + 1);
            //2. 准备数据，一定要转成字节数组
            String data3 = ackMark + " " + Seq3 + " " + ACK3;
//        System.out.println("+++++++++++++++++");
//        System.out.println(ACK3);
            //创建数据，并把数据打包
            clientMsg.sendMsg(data3, datagramSocket);

            System.out.println("====================");
            System.out.println("开始接收数据段...");
            byte[] bytes1 = new byte[1024];
            DatagramPacket datagramPacketD1 = new DatagramPacket(bytes1, bytes1.length);
            datagramSocket.receive(datagramPacketD1);
            String receiveMsg = new String(datagramPacketD1.getData(), 0, datagramPacketD1.getLength());
            System.out.println("接收到的数据段为:" + receiveMsg);
            String[] split = receiveMsg.split(" ");
            String SeqD1 = split[0];

            System.out.println("====================");
            System.out.println("数据消息确认返回");
            String replyMsg = "收到消息";
            clientMsg.sendMsg(replyMsg, datagramSocket);

            //四次挥手，关闭连接
            System.out.println("====================");
            System.out.println("四次挥手:");

            System.out.println("第一次挥手: 客户端 -> 服务端");
            System.out.println("数据发送...");
            connectionMarks.setFinMark(2);
            String finMark = String.valueOf(connectionMarks.getFinMark());
            connectionMarks.setACKMark(1);
            String ACKFin = String.valueOf(connectionMarks.getACKMark());
            String SeqFin = String.valueOf(connectionMarks.getSeq());
            String ACKS1 = String.valueOf(Integer.parseInt(SeqD1) + 1);
            String dataF1 = finMark + "/" + ACKFin + " " + SeqFin + " " + ACKS1;
            clientMsg.sendMsg(dataF1, datagramSocket);

            System.out.println("====================");
            System.out.println("开始接收数据段...");
            byte[] bytesB2 = new byte[1024];
            DatagramPacket datagramPacketB2 = new DatagramPacket(bytesB2, bytesB2.length);
            datagramSocket.receive(datagramPacketB2);
            String receiveMsgB2 = new String(datagramPacketB2.getData(), 0, datagramPacketB2.getLength());
            System.out.println("接收到的数据段为:" + receiveMsgB2);

            System.out.println("====================");
            System.out.println("开始接收数据段...");
            byte[] bytesB3 = new byte[1024];
            DatagramPacket datagramPacketB3 = new DatagramPacket(bytesB3, bytesB3.length);
            datagramSocket.receive(datagramPacketB3);
            String receiveMsgB3 = new String(datagramPacketB3.getData(), 0, datagramPacketB3.getLength());
            System.out.println("接收到的数据段为:" + receiveMsgB3);
            String[] splitB3 = receiveMsgB3.split(" ");
            String[] split2 = splitB3[0].split("/");
            if (
                    !(split2[0].equals("2")
                            || split2[1].equals("1")
                            ||splitB3[1].equals(ACKS1)
                            ||splitB3[2].equals(String.valueOf(Integer.parseInt(SeqFin) + 1)))
            ){
                throw new WrongConnectionException("非本次连接");
            }
            System.out.println("====================");
            System.out.println("第四次挥手: 客户端 -> 服务端");
            System.out.println("数据发送...");
            String ackMark4 = ACKFin;
            String SeqB4 = SeqFin;
            String ACKB4 = String.valueOf(Integer.parseInt(ACKS1) + 1);
            String dataB4 = ackMark4 + " " + SeqB4 + " " + ACKB4;
            clientMsg.sendMsg(dataB4, datagramSocket);

            //关闭流
            datagramSocket.close();
        } catch (RuntimeException e) {
            throw new RuntimeException("超时错误，请求连接失败");
        } catch (WrongConnectionException e){
            System.out.println("错误提醒: " + e.getMessage());
        }
    }
    public void sendMsg(String dataMsg, DatagramSocket datagramSocket) throws IOException {
        byte[] bytes = dataMsg.getBytes();
        DatagramPacket datagramPacketMsg = new DatagramPacket(bytes, 0,bytes.length, new InetSocketAddress("localhost",9999));
        datagramSocket.send(datagramPacketMsg);
    }
}
