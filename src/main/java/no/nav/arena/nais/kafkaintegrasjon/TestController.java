package no.nav.arena.nais.kafkaintegrasjon;

import io.prometheus.client.Counter;
import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaConsumer;
import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaMessage;
import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaProducer;
import no.nav.arena.nais.kafkaintegrasjon.kafka.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    private final KafkaProducer kafkaProducer;
    private final KafkaConsumer kafkaConsumer;
    private final Counter metricsCounter;

    @Autowired
    public TestController(KafkaProducer kafkaProducer, KafkaConsumer kafkaConsumer, Counter counter) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaConsumer = kafkaConsumer;
        this.metricsCounter = counter;
    }

    @GetMapping("/test")
    public String test(){
        metricsCounter.labels("/api/test").inc();

        kafkaProducer.sendMessage("TESTING YOLO", "topic1");
        return "OK";
    }

    /**
     * @return List of available topics
     */
    @GetMapping("/kafka/topics")
    public @ResponseBody
    List<String> kafkaTopics() {
        metricsCounter.labels("/api/kafka/topics").inc();

        return KafkaConfig.getKafkaTopics();
    }

    /**
     * @param message to be sent
     * @return HttpStatus
     */
    @PostMapping("/kafka/send-message")
    public ResponseEntity<String> sendMessageForm(@RequestBody KafkaMessage message) {
        metricsCounter.labels("/api/kafka/send-message").inc();

        kafkaProducer.sendMessage(message);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * @param topic to listen to
     * @return all messages from topic
     */
    @GetMapping("/kafka/consume-topic/{topic}")
    public @ResponseBody
    List<KafkaMessage> consumeTopic(@PathVariable String topic) {
        metricsCounter.labels("/api/kafka/consume-topic").inc();

        return kafkaConsumer.getConsumedMessages(topic);
    }
}
