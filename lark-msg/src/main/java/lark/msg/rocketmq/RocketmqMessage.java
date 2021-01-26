package lark.msg.rocketmq;

import lark.core.util.Times;
import lark.msg.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.time.Duration;
import java.time.LocalDateTime;

public class RocketmqMessage implements Message {
    private MessageExt msg;

    public RocketmqMessage(MessageExt msg) {
        this.msg = msg;
    }

    @Override
    public String getId() {
        return msg.getMsgId();
    }

    @Override
    public String getBody() {
        return new String( msg.getBody() );
    }

    @Override
    public LocalDateTime getSendTime() {
        return Times.toLocalDateTime( msg.getBornTimestamp() );
    }

    @Override
    public LocalDateTime getReceiveTime() {
        return Times.toLocalDateTime( msg.getReconsumeTimes() );
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
