package lark.util.msg;

import lark.core.data.Guid;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 消息服务
 * eg：
 * @Autowired
 * MessageService messageService;
 *
 * messageService.send( OrderTopic.CREATE_ORDER, order );
 *
 * @author Andy
 */
@Component
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    public static final int TIMEOUT = 3 * 60 * 1000;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private MessageService(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public boolean send( String topic, Object msg )
    {
        return send( topic, msg, Duration.ZERO );
    }

    public boolean send( String topic, Object msg, Duration delaySecond ) {
        SendResult result = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload( msg ).build(), TIMEOUT, convertToDelayLevel( ( int ) delaySecond.getSeconds() ) );
        SendStatus sendStatus = result.getSendStatus();
        return sendStatus == SendStatus.SEND_OK;
    }

    int convertToDelayLevel( int delaySecond ) {
        return MessageDelayLevel.getClosestBySeconds( delaySecond ).getLevel();
    }

}
