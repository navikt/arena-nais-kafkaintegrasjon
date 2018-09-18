package no.nav.arena.nais.kafkaintegrasjon;

import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaEmbeddedServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunApplication {


    public static void main(String[] args) {
        KafkaEmbeddedServer.startEmbeddedKafka();
        SpringApplication.run(RunApplication.class, args);
    }

}
