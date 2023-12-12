package TCP_handShake;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 初始化Seq的值ISN
 * RFC1948中提出了一个较好的初始化序列号ISN随机生成算法：
 * ISN = M + F(localhost, localport, remotehost, remoteport).
 *
 */
public class initializeISN {
    private int ISN = generateISN() ;

    public int getISN() {
        return ISN;
    }

    private int generateISN(){
        // 获取当前时间
        String currentTime = String.valueOf(LocalDateTime.now().getSecond());

        // 生成UUID
        UUID uuid = UUID.randomUUID();

        // 将时间和UUID结合生成ISN
        String isnString = currentTime + uuid.toString();
        int isn = isnString.hashCode();

        return isn;
    }
}


