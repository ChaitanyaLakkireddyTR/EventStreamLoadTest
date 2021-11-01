package com.tr.kafka.load.medium;

import com.tr.kafka.load.high.HighFileLoadService;
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

public class MediumFileLoadService {

    private static final Logger logger = LoggerFactory.getLogger(MediumFileLoadService.class);

    @Autowired
    @Qualifier("mediumLoadKafkaTemplate")
    private KafkaTemplate<String, Object> mediumLoadKafkaTemplate;

    @Value("${mediumtopic.name}")
    private String mediumTopic;


    @Transactional("mediumLoadKafkaTransactionManager")
    public String uploadFile(File file) throws IOException {
        Long fileSize = file.length();
        final ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(mediumTopic, Files.readAllBytes(file.toPath()));
        producerRecord.headers().add("fileName", file.getName().getBytes(StandardCharsets.UTF_8));
        long startTime = System.currentTimeMillis();
        mediumLoadKafkaTemplate.executeInTransaction(t -> {
            return t.send(producerRecord);
        });
        long endTime = System.currentTimeMillis();
        LocalDateTime timeStamp = LocalDateTime.now();
        logger.info("Total execution time====>{} to process file of size: {} and timeStamp {} ", (endTime - startTime), fileSize, timeStamp);
        return "Published successfully";
    }
}
