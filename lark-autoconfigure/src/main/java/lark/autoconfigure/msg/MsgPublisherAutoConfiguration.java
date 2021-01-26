package lark.autoconfigure.msg;

import lark.msg.Publisher;
import lark.msg.rocketmq.RocketmqPublisher;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
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
@ConditionalOnClass({Publisher.class})
@ConditionalOnProperty(prefix="lark.msg",name = "publisher", havingValue = "true")
@EnableConfigurationProperties(MsgServiceProperties.class)
public class MsgPublisherAutoConfiguration {

    @Autowired
    Environment environment;

    @Bean
    @ConditionalOnMissingBean
    public DefaultMQProducer producer(MsgServiceProperties props) throws MQClientException {
        DefaultMQProducer mqProducer = new DefaultMQProducer( environment.getProperty( "spring.application.name" ));
        mqProducer.setNamesrvAddr( props.getAddress() );
        mqProducer.start();
        return mqProducer;
    }

    @Bean
    @ConditionalOnMissingBean
    public Publisher publisher( DefaultMQProducer producer ) {
        return new RocketmqPublisher( producer );
    }
}
