package no.nav.arena.nais.kafkaintegrasjon.kafka;

import no.nav.common.KafkaEnvironment;
import no.nav.common.embeddedutils.ServerBase;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaEmbeddedServer {

    private static KafkaEnvironment kafkaEnvironment;

    public static KafkaEnvironment getKafkaEnvironment(){
        return kafkaEnvironment;
    }

    public static void startEmbeddedKafka(){
        if (kafkaEnvironment != null) return;

        kafkaEnvironment = new KafkaEnvironment(
                1,
                Arrays.asList("topic1"),
                false,
                false,
                false
        );


        String brokers = kafkaEnvironment.getServerPark().getBrokers().
                stream().map(ServerBase::getUrl).
                collect(Collectors.joining(","));

        kafkaEnvironment.start();
        System.setProperty("kafkaHost", brokers);
    }
}
