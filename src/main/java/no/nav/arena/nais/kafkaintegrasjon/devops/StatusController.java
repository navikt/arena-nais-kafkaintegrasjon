package no.nav.arena.nais.kafkaintegrasjon.devops;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class StatusController {

	// this endpoint is required by NAIS platform, it is used to check the status of microservices-app
	@GetMapping("/internal/status")
	public ResponseEntity<String> isAlive() {
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
