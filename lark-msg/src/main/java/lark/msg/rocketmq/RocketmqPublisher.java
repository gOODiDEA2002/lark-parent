package lark.msg.rocketmq;

import lark.core.codec.JsonCodec;
import lark.msg.Publisher;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andy
 */
public class RocketmqPublisher implements Publisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketmqPublisher.class);
    private static final String ERROR = "try publish failed";

    private DefaultMQProducer producer;

    public RocketmqPublisher( DefaultMQProducer producer ) {
        this.producer = producer;
    }

    @Override
    public void publish(String topic, Object data ) {
        publish( topic, data, Duration.ZERO );
    }

    @Override
    public void publish(String topic, Object data, Duration delay) {
        safePublish( topic, data, delay );
    }

    @Override
    public <T> void publishBatch(String topic, List<T> datas ) {
        List<Message> messages = new ArrayList<>(datas.size());
        for (T data : datas) {
            Message msg = new Message(topic, JsonCodec.encodeAsBytes( data ) );
            messages.add(msg);
        }
        safePublish( topic, datas );
    }

    private boolean safePublish( String topic, Object data, Duration delay ) {
        try {
            Message msg = new Message(topic, JsonCodec.encodeAsBytes( data ) );
            msg.setDelayTimeLevel( convertToDelayLevel( ( int ) delay.getSeconds() ) );
            SendResult result = producer.send( msg );
            SendStatus sendStatus = result.getSendStatus();
            return sendStatus == SendStatus.SEND_OK;
        } catch (Exception e) {
            LOGGER.error(ERROR, e);
            return false;
        }
    }

    private <T> boolean safePublish( String topic, List<T> msgs ) {
        List<Message> messages = new ArrayList<>(msgs.size());
        for (Object data : msgs) {
            Message msg = new Message(topic, JsonCodec.encodeAsBytes( data ) );
            messages.add(msg);
        }
        //
        try {
            SendResult result = producer.send( messages );
            SendStatus sendStatus = result.getSendStatus();
            return sendStatus == SendStatus.SEND_OK;
        } catch (Exception e) {
            LOGGER.error(ERROR, e);
            return false;
        }
    }

    private int convertToDelayLevel( int delaySecond ) {
        return RocketMqDelayLevel.getClosestBySeconds( delaySecond ).getLevel();
    }
}
