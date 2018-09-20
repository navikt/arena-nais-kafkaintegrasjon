package no.nav.arena.nais.kafkaintegrasjon.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public synchronized void sendMessage(String msg, String topic){
        System.out.println("\nSending message on topic " + topic + "\n");
        kafkaTemplate.send(topic, msg);
    }

    public synchronized void sendMessage(KafkaMessage message){
        System.out.println("\nSending message on topic " + message.getTopic() + "\n");
        kafkaTemplate.send(message.getTopic(), message.getText());
    }
}
