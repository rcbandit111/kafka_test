package org.engine.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.engine.plugin.transactions.factory.SaleRequestFactory;
import org.engine.plugin.transactions.factory.SaleRequestFactorySerializer;
import org.engine.plugin.transactions.factory.SaleResponseFactory;
import org.engine.plugin.transactions.factory.SaleResponseFactoryDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, SaleRequestFactory> saleRequestFactoryProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SaleRequestFactorySerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SaleRequestFactory> saleRequestFactoryKafkaTemplate() {
        return new KafkaTemplate<>(saleRequestFactoryProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, SaleResponseFactory> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tp-sale.reply");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SaleResponseFactoryDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SaleResponseFactory> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SaleResponseFactory> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ReplyingKafkaTemplate<String, SaleRequestFactory, SaleResponseFactory> replyKafkaTemplate(ProducerFactory<String, SaleRequestFactory> producerFactory, ConcurrentKafkaListenerContainerFactory<String, SaleResponseFactory> factory) {
        ConcurrentMessageListenerContainer<String, SaleResponseFactory> kafkaMessageListenerContainer = factory.createContainer("tp-sale.reply");
        ReplyingKafkaTemplate<String, SaleRequestFactory, SaleResponseFactory> requestReplyKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, kafkaMessageListenerContainer);
        requestReplyKafkaTemplate.setDefaultTopic("tp-sale.reply");
        return requestReplyKafkaTemplate;
    }
}

