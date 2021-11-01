package com.tr.kafka.load.high;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;


public class HighFileLoadService {


    private static final Logger logger = LoggerFactory.getLogger(HighFileLoadService.class);

    @Autowired
    @Qualifier("highLoadKafkaTemplate")
    private KafkaTemplate<String, Object> highLoadKafkaTemplate;

    @Value("${hightopic.name}")
    private String highTopic;


    @Transactional("highLoadKafkaTransactionManager")
    public String uploadFile(File file) throws IOException {
        Long fileSize = file.length();
        final ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(highTopic, Files.readAllBytes(file.toPath()));
        producerRecord.headers().add("fileName", file.getName().getBytes(StandardCharsets.UTF_8));
        long startTime = System.currentTimeMillis();
        highLoadKafkaTemplate.executeInTransaction(t -> {
            return t.send(producerRecord);
        });
        long endTime = System.currentTimeMillis();
        LocalDateTime timeStamp = LocalDateTime.now();
        logger.info("Total execution time====>{} to process file of size: {} and timeStamp {} ", (endTime - startTime), fileSize, timeStamp);
        return "Published successfully";
    }
}


