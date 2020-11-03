package lark.util.msg;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy Yuan on 2020/10/28.
 */
public class RocketMQMessage implements Message {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public LocalDateTime getSendTime() {
        return null;
    }

    @Override
    public LocalDateTime getReceiveTime() {
        return null;
    }

    @Override
    public int getAttempts() {
        return 0;
    }

    @Override
    public void finish() {

    }

    @Override
    public void requeue(Duration delay) {

    }

    @Override
    public void requeue() {

    }
}
