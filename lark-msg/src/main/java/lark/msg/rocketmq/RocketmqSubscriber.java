package lark.msg.rocketmq;

import lark.msg.Subscriber;
import lark.msg.Handler;
import lark.msg.Subscription;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RocketmqSubscriber implements Subscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketmqSubscriber.class);

    private DefaultMQPushConsumer consumer;

    public RocketmqSubscriber( DefaultMQPushConsumer consumer ) {
        this.consumer = consumer;
    }

    @Override
    public void subscribe(Subscription sub) {
        subscribe(sub.getTopic(), sub.getChannel(), sub.getThreads(), sub.getHandler());
    }

    @Override
    public void subscribe(String topic, String channel, int threads, Handler handler) {
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setClientCallbackExecutorThreads(threads);
        try {
            consumer.subscribe( topic, "*");
        } catch (MQClientException e) {
            LOGGER.error( "RocketmqSubscriber subscribe fail: {}", e.getMessage());
        }
        //
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for ( MessageExt msg : msgs ) {
                    handler.handle( new RocketmqMessage( msg ));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //
        try {
            consumer.start();
        } catch (MQClientException e) {
            LOGGER.error( "RocketmqSubscriber start fail: {}", e.getMessage());
        }
    }
}
