package no.nav.arena.nais.kafkaintegrasjon.kafka;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaConsumer {

    @KafkaListener(topics = "topic1", groupId = "test")
    public void listen(@Payload String message){
        System.out.println(message);
    }
}
