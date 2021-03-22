package lark.util.number;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;

import static java.lang.Integer.parseInt;


/**
 * 生成uuid
 */
public class UuidUtil {

    private static final Log log = LogFactory.get();

    private static long workerId = -1;

    private static long dataId = RandomUtil.randomInt(0, 31);

    private static Snowflake snowflake = null;


    private static void getWorkId() {
        if (workerId == -1) {
            try {
                String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                int[] ints = StringUtils.toCodePoints(hostAddress);
                int sums = 0;
                for (int b : ints) {
                    sums += b;
                }
                workerId = (sums % 32);
            } catch (Exception e) {
                log.error("获取机器 ID 失败{}->随机获取->{}", e, workerId);
            } finally {
                if (workerId == 0) {
                    workerId = Long.valueOf(RandomUtil.randomInt(0, 31));
                }
            }
            log.info("workerId->{}", workerId);
        }
    }


    /**
     * 生成uuid
     *
     * @return
     */
    public static String UUID() {
        getWorkId();
        if (snowflake == null) {
            snowflake = IdUtil.createSnowflake(workerId, dataId);
        }
        return snowflake.nextIdStr();
    }

    /**
     * 生成订单号,22位
     *
     * @return
     */
    public static String ORDER_SN() {
        return batchId(parseInt(RandomUtil.randomNumbers(5)), parseInt(RandomUtil.randomNumbers(2)));
    }

    /**
     * 获取一个批次号，形如 2019071015301361000101237
     * <p>
     * 数据库使用 char(25) 存储
     *
     * @param tenantId 租户ID，5 位
     * @param module   业务模块ID，2 位
     * @return 返回批次号
     */
    protected synchronized static String batchId(int tenantId, int module) {
        String prefix = DateTime.now().toString(DatePattern.PURE_DATETIME_MS_PATTERN);
        return prefix + tenantId + module + UUID().substring(3);
    }


}
