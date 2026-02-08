package org.ko4inage.requestprocessor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.ACKS_CONFIG, "all"); // ждем подтверждения
        configProperties.put(ProducerConfig.RETRIES_CONFIG, 1);
        configProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000); // ждем метаданные топика 5 секунд вместо 60

        return new DefaultKafkaProducerFactory<>(
                configProperties
        );
    };

    @Bean
    public KafkaTemplate<String,String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


}
