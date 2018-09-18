package no.nav.arena.nais.kafkaintegrasjon;

import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final KafkaProducer kafkaProducer;

    @Autowired
    public TestController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    @GetMapping("/test")
    public String test(){
        kafkaProducer.sendMessage("TESTING YOLO", "topic1");
        return "OK";
    }
}
