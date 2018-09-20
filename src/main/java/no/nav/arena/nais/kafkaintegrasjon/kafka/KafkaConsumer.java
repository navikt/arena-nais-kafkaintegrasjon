package no.nav.arena.nais.kafkaintegrasjon.kafka;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableKafka
public class KafkaConsumer {

    private List<KafkaMessage> consumedMessages = new ArrayList();

    @KafkaListener(topics = "#{'${arena-nais-kafkaintegrasjon.kafka-topics}'.split(',')}", groupId = "test")
    public void listen(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        System.out.println("Recieved message " + message + " @ " + topic);
        consumedMessages.add(new KafkaMessage(message, topic));
    }

    public List<KafkaMessage> getConsumedMessages(String topic) {
        return consumedMessages
                .stream()
                .filter(message -> message.getTopic().equals(topic))
                .collect(Collectors.toList());
    }
}
