package io.bayrktlihn.kafkatutorial;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class KafkaTutorialApplication {
    public static void main(String[] args) {
        printBrokers();
    }

    private static void printBrokers() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            DescribeClusterResult describeClusterResult = adminClient.describeCluster();
            System.out.println("Cluster ID: " + describeClusterResult.clusterId().get());
            System.out.println("Brokers: " + describeClusterResult.nodes().get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
