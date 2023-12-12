package TCP_handShake;

/**
 * 标志位 connectionMarks
 */
public class ConnectionMarks extends initializeISN{
    //每次建立新连接，将SYN初始化为1
    private int SYN;
    //随机
    private int Seq;

    private  int ACKMark;

    public int getFinMark() {
        return FinMark;
    }

    public void setFinMark(int finMark) {
        FinMark = finMark;
    }

    private int FinMark;

    public int getACKMark() {
        return ACKMark;
    }

    public void setACKMark(int ACKMark) {
        this.ACKMark = ACKMark;
    }

    public ConnectionMarks() {
        this.SYN = 1;
        this.Seq = getISN();
        this.ACKMark = 0;
        this.FinMark = 0;
    }


    public int getSeq() {
        return Seq;
    }


    //setter of SYN
    public Integer getSYN() {
        return SYN;
    }
}
