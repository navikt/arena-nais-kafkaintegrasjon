package no.nav.arena.nais.kafkaintegrasjon.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public synchronized void sendMessage(String msg, String topic){
        System.out.println("\nSending message\n");
        kafkaTemplate.send(topic, msg);
    }
}
