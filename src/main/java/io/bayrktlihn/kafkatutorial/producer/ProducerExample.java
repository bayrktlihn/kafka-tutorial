package io.bayrktlihn.kafkatutorial.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

@Slf4j
public class ProducerExample {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "host.docker.internal:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());


        Scanner scanner = new Scanner(System.in);

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        System.out.println("Give me a value: ");
        String value = scanner.nextLine();
        while (value != null) {
            System.out.println("typed "+value);
            kafkaProducer.send(new ProducerRecord<>("topic1","1", value), (metadata, exception) -> {
                if (exception != null) {
                    log.info("{}", exception);
                } else {
                    log.info("Topic: {}, Partition: {}, Offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
            System.out.println("Give me a value: ");
            value = scanner.nextLine();
        }


        kafkaProducer.flush();

        kafkaProducer.close();
    }
}
