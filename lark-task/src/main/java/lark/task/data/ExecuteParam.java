package lark.task.data;

import lark.core.lang.EnumValuable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 执行参数
 * @author cuigh
 */
@Getter
@Setter
public class ExecuteParam {
    /**
     * 执行类型, 0-自动, 1-手动
     */
    private ExecuteType type = ExecuteType.MANUAL;

    private ExecuteLogger logger;

    /**
     * 任务唯一标识
     */
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务别名
     */
    private String alias;

    /**
     * 参数
     */
    private List<Arg> args;

    /**
     * 日志ID（XXL-JOB）
     */
    private int logId;

    /**
     * 日志时间（XXL-JOB）
     */
    private long logDateTime;

    /**
     * 执行方式
     */
    public enum ExecuteType implements EnumValuable {
        /**
         * 自动
         */
        AUTO(0),

        /**
         * 手动
         */
        MANUAL(1);

        private int value;

        ExecuteType(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return this.value;
        }
    }
}

