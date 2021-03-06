package no.nav.arena.nais.kafkaintegrasjon.kafka;

import no.nav.arena.nais.kafkaintegrasjon.kafka.config.KafkaConfig;
import no.nav.common.KafkaEnvironment;
import no.nav.common.embeddedutils.ServerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class KafkaEmbeddedServer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaEmbeddedServer.class);

    private static KafkaEnvironment kafkaEnvironment;

    public static KafkaEnvironment getKafkaEnvironment(){
        return kafkaEnvironment;
    }

    public static void startEmbeddedKafka(){
        if (kafkaEnvironment != null) return;

        kafkaEnvironment = new KafkaEnvironment(
                1,
                KafkaConfig.getKafkaTopics(),
                false,
                false,
                false
        );


        String brokers = kafkaEnvironment.getServerPark().getBrokers().
                stream().map(ServerBase::getUrl).
                collect(Collectors.joining(","));

        kafkaEnvironment.start();
        LOG.info("Embedded Kafka started with brokers {}", brokers);
        System.setProperty("kafkaHost", brokers);
    }
}
