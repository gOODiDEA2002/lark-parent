package lark.autoconfigure.msg;

import lark.msg.MsgHandlerService;
import lark.msg.Subscriber;
import lark.msg.api.HandlerApi;
import lark.msg.rocketmq.RocketmqSubscriber;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author andy
 */
@Configuration
@ConditionalOnClass({Subscriber.class})
@ConditionalOnProperty(prefix="lark.msg",name = "subscriber", havingValue = "true")
@EnableConfigurationProperties(MsgServiceProperties.class)
public class MsgSubscriberAutoConfiguration {

    @Autowired
    Environment environment;

    @Bean
    @ConditionalOnMissingBean
    public DefaultMQPushConsumer consumer(MsgServiceProperties props) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer( environment.getProperty( "spring.application.name" ));
        consumer.setNamesrvAddr( props.getAddress() );
        return consumer;
    }

    @Bean
    @ConditionalOnMissingBean
    public Subscriber subscriber( DefaultMQPushConsumer consumer ) {
        return new RocketmqSubscriber( consumer );
    }

    @Bean
    @ConditionalOnMissingBean
    public MsgHandlerService handlerService() {
        return new MsgHandlerService();
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerApi handlerApi( MsgHandlerService handlerService ) {
        return new HandlerApi( handlerService );
    }
}
