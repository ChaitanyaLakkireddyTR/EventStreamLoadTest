
package com.tr.kafka.load.comfig;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LoadConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }


    @Bean(name = "highLoadKafkaTransactionManager")
    public KafkaTransactionManager highloadKafkTransactionMaanger(KafkaProperties kafkaProperties) {
        return new KafkaTransactionManager(highLoadProducerFactory(kafkaProperties));
    }

    @Bean(name = "highLoadProducerConfig")
    public Map<String, Object> highLoadProducerConfig(KafkaProperties kafkaProperties) {
        Map<String, Object> highLoadProducerConfigMap = new HashMap<>(kafkaProperties.buildProducerProperties());
        highLoadProducerConfigMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        highLoadProducerConfigMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        highLoadProducerConfigMap.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10145860);

        return highLoadProducerConfigMap;
    }

    @Bean(name = "highLoadProducerFactory")
    public ProducerFactory<String, Object> highLoadProducerFactory(KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(highLoadProducerConfig(kafkaProperties));
        defaultKafkaProducerFactory.setTransactionIdPrefix("fast");
        return defaultKafkaProducerFactory;
    }

    @Bean(name = "highLoadKafkaTemplate")
    public KafkaTemplate<String, Object> highLoadKafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(highLoadProducerFactory(kafkaProperties));

    }

    @Bean(name = "mediumLoadKafkaTransactionManager")
    public KafkaTransactionManager mediumloadKafkTransactionMaanger(KafkaProperties kafkaProperties) {
        return new KafkaTransactionManager(mediumLoadProducerFactory(kafkaProperties));
    }

    @Bean(name = "mediumLoadProducerConfig")
    public Map<String, Object> mediumLoadProducerConfig(KafkaProperties kafkaProperties) {
        Map<String, Object> mediumLoadProducerConfigMap = new HashMap<>(kafkaProperties.buildProducerProperties());
        mediumLoadProducerConfigMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        mediumLoadProducerConfigMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        mediumLoadProducerConfigMap.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10145860);

        return mediumLoadProducerConfigMap;
    }

    @Bean(name = "mediumLoadProducerFactory")
    public ProducerFactory<String, Object> mediumLoadProducerFactory(KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(mediumLoadProducerConfig(kafkaProperties));
        defaultKafkaProducerFactory.setTransactionIdPrefix("fast");
        return defaultKafkaProducerFactory;
    }

    @Bean(name = "mediumLoadKafkaTemplate")
    public KafkaTemplate<String, Object> mediumLoadKafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(mediumLoadProducerFactory(kafkaProperties));

    }

    @Bean(name = "lowLoadKafkaTransactionManager")
    public KafkaTransactionManager lowloadKafkTransactionMaanger(KafkaProperties kafkaProperties) {
        return new KafkaTransactionManager(lowLoadProducerFactory(kafkaProperties));
    }

    @Bean(name = "lowLoadProducerConfig")
    public Map<String, Object> lowLoadProducerConfig(KafkaProperties kafkaProperties) {
        Map<String, Object> lowLoadProducerConfigMap = new HashMap<>(kafkaProperties.buildProducerProperties());
        lowLoadProducerConfigMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        lowLoadProducerConfigMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        lowLoadProducerConfigMap.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10145860);

        return lowLoadProducerConfigMap;
    }

    @Bean(name = "lowLoadProducerFactory")
    public ProducerFactory<String, Object> lowLoadProducerFactory(KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(lowLoadProducerConfig(kafkaProperties));
        defaultKafkaProducerFactory.setTransactionIdPrefix("fast");
        return defaultKafkaProducerFactory;
    }

    @Bean(name = "lowLoadKafkaTemplate")
    public KafkaTemplate<String, Object> lowLoadKafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(lowLoadProducerFactory(kafkaProperties));

    }

}

