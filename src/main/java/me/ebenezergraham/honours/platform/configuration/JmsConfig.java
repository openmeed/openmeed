package me.ebenezergraham.honours.platform.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;


@Configuration
public class JmsConfig {

    final String LOGGER_NAME = "command-logger";
    final String SERIALIZER = "command-serializer";
    String COMMAND_SOURCE_TABLE_NAME = "command_source";

    String APPLICATION_NAME_PROP = "spring.application.name";
    String APPLICATION_NAME_DEFAULT = "command-v1";

    String ACTIVEMQ_BROKER_URL_PROP = "activemq.brokerUrl";
    String ACTIVEMQ_BROKER_URL_DEFAULT = "vm://localhost?broker.persistent=false";
    String ACTIVEMQ_CONCURRENCY_PROP = "activemq.concurrency";
    String ACTIVEMQ_CONCURRENCY_DEFAULT = "3-10";

    @Bean
    public Queue queue(){
        return new ActiveMQQueue("merge-queue");
    }

    @Bean(name = SERIALIZER)
    public Gson gson() {
        return new GsonBuilder().create();
    }

    @Bean(name = LOGGER_NAME)
    public Logger loggerBean() {
        return LoggerFactory.getLogger(LOGGER_NAME);
    }

    @Bean
    public PooledConnectionFactory jmsFactory() {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        final ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(ACTIVEMQ_BROKER_URL_DEFAULT);
        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return pooledConnectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(final PooledConnectionFactory jmsFactory) {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(jmsFactory);
        factory.setConcurrency(ACTIVEMQ_CONCURRENCY_DEFAULT);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(final PooledConnectionFactory jmsFactory) {
        final ActiveMQTopic activeMQTopic = new ActiveMQTopic("merge-topic");
        final JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setConnectionFactory(jmsFactory);
        jmsTemplate.setDefaultDestination(activeMQTopic);
        return jmsTemplate;
    }
}
