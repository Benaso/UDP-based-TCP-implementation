package TCP_handShake;

public class Timer implements Runnable{

    private int seconds;
    private int ShutDownMark;

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    //初始化seconds和ShoutDownMark;
    public Timer() {
        this.seconds = 0;
        ShutDownMark = 0;
    }


    @Override
    public void run() {
        //在75秒内收到收到信息则开始建立连接，如果75秒内没有收到则不建立连接。
        while (seconds < 10){
            try {
                Thread.sleep(1000);
                seconds++;
                System.out.println(seconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        说明超过75秒
        ShutDownMark = 1;
    }

    public int getShutDownMark() {
        return ShutDownMark;
    }
}
