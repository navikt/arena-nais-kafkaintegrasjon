package no.nav.arena.nais.kafkaintegrasjon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
/*
@EmbeddedKafka(partitions = 1,
    topics = {"TEST_TOPIC", "DEFAULT_TOPIC"}
)*/
@EnableKafka
public class KafkaEmbeddedConfig {


    //@Autowired
    public  KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, false, "TEST_TOPIC");

    //@Autowired
    //private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;



}
