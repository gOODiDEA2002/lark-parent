package lark.msg;

import lark.msg.Handler;
import lark.msg.Subscription;

/**
 * @author cuigh
 */
public interface Subscriber {
    /**
     * 订阅消息
     *
     * @param sub 订阅信息
     */
    void subscribe(Subscription sub);

    void subscribe(String topic, String channel, int threads, Handler handler);
}
