package io.bayrktlihn.kafkatutorial.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class ConsumerExample {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // consumer group yoksa başkan aşağı oku. Varsa zaten current ofsetden oku
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // consumer group yoksa son ofsetden oku. Varsa zaten current ofsetden oku
        properties.put(CommonClientConfigs.GROUP_ID_CONFIG, "my-consumer-group");

        Thread mainThread = Thread.currentThread();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.wakeup();

            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        consumer.subscribe(List.of("topic1"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("{}", record);
                }
            }
        } catch (WakeupException e) {
            log.info("WakeupException caught");
        }

    }
}
