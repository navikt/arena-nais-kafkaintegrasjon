package no.nav.arena.nais.kafkaintegrasjon;

import io.prometheus.client.Counter;
import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final KafkaProducer kafkaProducer;
    private final Counter metricsCounter;

    @Autowired
    public TestController(KafkaProducer kafkaProducer, Counter counter) {
        this.kafkaProducer = kafkaProducer;
        this.metricsCounter = counter;
    }

    @GetMapping("/test")
    public String test(){
        metricsCounter.labels("/api/test").inc();

        kafkaProducer.sendMessage("TESTING YOLO", "topic1");
        return "OK";
    }
}
