package no.nav.arena.nais.kafkaintegrasjon.devops;

import io.prometheus.client.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class StatusController {

    private final Counter metricsCounter;

    @Autowired
    public StatusController(Counter counter) {
        this.metricsCounter = counter;
    }

	// this endpoint is required by NAIS platform, it is used to check the status of microservices-app
	@GetMapping("/internal/status")
	public ResponseEntity<String> isAlive() {
        metricsCounter.labels("/api/internal/status").inc();

		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
