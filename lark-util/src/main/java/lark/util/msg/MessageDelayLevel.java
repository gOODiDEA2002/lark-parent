package lark.util.msg;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <b>消息延迟级别</b>
 * </pre>
 * ported from:
 * https://gist.githubusercontent.com/davelet/01407e61c77b212be733155c3ebbad7b/raw/220911016d13d737ee6ce7939942adea1c9158df/MessageDelayLevelEnum.java
 * @author weixiaodong yhid: 80752866
 */
public enum MessageDelayLevel {
    L0_NO_DELAY(0, "不延时", 0, TimeUnit.SECONDS),
    L1_ONE_SEC(1, "1秒钟", 1, TimeUnit.SECONDS),
    L2_FIVE_SEC(2, "5秒钟", 5, TimeUnit.SECONDS),
    L3_TEN_SEC(3, "10秒钟", 10, TimeUnit.SECONDS),
    L4_THIRTY_SEC(4, "30秒钟", 30, TimeUnit.SECONDS),
    L5_ONE_MIN(5, "1分钟", 1, TimeUnit.MINUTES),
    L6_TWO_MIN(6, "2分钟", 2, TimeUnit.MINUTES),
    L7_THREE_MIN(7, "3分钟", 3, TimeUnit.MINUTES),
    L8_FOUR_MIN(8, "4分钟", 4, TimeUnit.MINUTES),
    L9_FIVE_MIN(9, "5分钟", 5, TimeUnit.MINUTES),
    L10_SIX_MIN(10, "6分钟", 6, TimeUnit.MINUTES),
    L11_SEVEN_MIN(11, "7分钟", 7, TimeUnit.MINUTES),
    L12_EIGHT_MIN(12, "8分钟", 8, TimeUnit.MINUTES),
    L13_NINE_MIN(13, "9分钟", 9, TimeUnit.MINUTES),
    L14_TEN_MIN(14, "10分钟", 10, TimeUnit.MINUTES),
    L15_TWENTY_MIN(15, "20分钟", 20, TimeUnit.MINUTES),
    L16_THIRTY_MIN(16, "30分钟", 30, TimeUnit.MINUTES),
    L17_ONE_HOUR(17, "1小时", 1, TimeUnit.HOURS),
    L18_TWO_HOUR(18, "2小时", 2, TimeUnit.HOURS),
    ;

    private final int level;
    private final String description;
    private final int value;
    private final TimeUnit unit;

    MessageDelayLevel(int level, String desc, int value, TimeUnit unit) {
        this.value = value;
        this.description = desc;
        this.level = level;
        this.unit = unit;
    }

    /**
     * 根据延迟秒数匹配延迟最近的枚举，不会匹配失败所以不会报错
     *
     * @param sec 延迟秒数
     * @return 匹配的级别
     */
    public static MessageDelayLevel getClosestBySeconds(int sec) {
        int id = 0;
        while (values()[id].getUnit().toSeconds(values()[id].getValue()) < sec) {
            id++;
        }
        if (id == 0) {
            return L0_NO_DELAY;
        }
        if (id == values().length) {
            return L18_TWO_HOUR;
        }
        int lDiff = (int) (sec - values()[id - 1].getUnit().toSeconds(values()[id - 1].getValue()));
        int rDiff = (int) (values()[id].getUnit().toSeconds(values()[id].getValue()) - sec);
        if (lDiff <= rDiff) return values()[id - 1];
        return values()[id];
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}