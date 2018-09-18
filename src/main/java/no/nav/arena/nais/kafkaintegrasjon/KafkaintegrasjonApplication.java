package no.nav.arena.nais.kafkaintegrasjon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class KafkaintegrasjonApplication {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC_NAME = "test-topic";
    private static final String GROUP_NAME = "test-consumer-group";

    public static void main(String[] args) {
        SpringApplication.run(KafkaintegrasjonApplication.class, args);
    }

    @PostConstruct
    public void start() {
        sendMessage();
    }

    private void sendMessage() {
        kafkaTemplate.send(TOPIC_NAME, "HELLO WORLD!!");
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_NAME)
    private void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
