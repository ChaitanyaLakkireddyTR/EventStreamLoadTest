
package com.tr.kafka.load;


import com.tr.kafka.load.high.HighFileLoadService;
import com.tr.kafka.load.low.LowFileLoadService;
import com.tr.kafka.load.medium.MediumFileLoadService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class, DataSourceAutoConfiguration.class} )
@EnableTransactionManagement
@EnableScheduling
public class KafkaLoadTestApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(KafkaLoadTestApplication.class, args);


    }

    @Bean
    public HighFileLoadService highFileLoadService() {
        return new HighFileLoadService();
    }

    @Bean
    public MediumFileLoadService mediumFileLoadService() {
        return new MediumFileLoadService();
    }

    @Bean
    public LowFileLoadService lowFileLoadService() {
        return new LowFileLoadService();
    }

    @Override
    public void run(String... args) throws Exception {
        String directory = args[1];
        File folder = new File(directory);

        HighFileLoadService highFileLoadService = highFileLoadService();
        MediumFileLoadService mediumFileLoadService = mediumFileLoadService();
        LowFileLoadService lowFileLoadService = lowFileLoadService();

        if (folder.exists()) {
            File[] fileList = folder.listFiles();
            for (File file : fileList) {
                long bytes = file.length();
                long fileSizeInMb = bytes / (1024 * 1024);
                try {
                    if (fileSizeInMb <= 1) {
                        lowFileLoadService.uploadFile(file);
                    } else if (fileSizeInMb > 1 && fileSizeInMb <= 2) {
                        mediumFileLoadService.uploadFile(file);
                    } else {
                        highFileLoadService.uploadFile(file);
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

