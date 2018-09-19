package no.nav.arena.nais.kafkaintegrasjon.redis.controller;

import io.prometheus.client.Counter;
import no.nav.arena.nais.kafkaintegrasjon.redis.model.Customer;
import no.nav.arena.nais.kafkaintegrasjon.redis.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/redis")
public class WebController {

	@Autowired
	private CustomerRepository customerRepository;
	private final Counter httpRequestsTotal;

	@Autowired
    public WebController(Counter counter) {
        httpRequestsTotal = counter;
	}

	@RequestMapping("/save")
	public String save() {
		// save a single Customer
		customerRepository.save(new Customer(1, "Jack", "Smith"));
		customerRepository.save(new Customer(2, "Adam", "Johnson"));
		customerRepository.save(new Customer(3, "Kim", "Smith"));
		customerRepository.save(new Customer(4, "David", "Williams"));
		customerRepository.save(new Customer(5, "Peter", "Davis"));

		// Prometheus - tell opp antall requests
		httpRequestsTotal.labels("/save").inc();

		return "Done";
	}

	@RequestMapping("/findall")
	public String findAll() {
		String result = "";
		Map<Long, Customer> customers = customerRepository.findAll();

		for (Customer customer : customers.values()) {
			result += customer.toString() + "<br>";
		}

		// Prometheus - tell opp antall requests
		httpRequestsTotal.labels("/findall").inc();

		return result;
	}

	@RequestMapping("/find")
	public String findById(@RequestParam("id") Long id) {
		String result = "";
		result = customerRepository.find(id).toString();

		// Prometheus - tell opp antall requests
		httpRequestsTotal.labels("/find-id").inc();

		return result;
	}

	@RequestMapping(value = "/uppercase")
	public String postCustomer(@RequestParam("id") Long id) {
		Customer customer = customerRepository.find(id);
		customer.setFirstName(customer.getFirstName().toUpperCase());
		customer.setLastName(customer.getLastName().toUpperCase());

		customerRepository.update(customer);

		// Prometheus - tell opp antall requests
		httpRequestsTotal.labels("/uppercase-id").inc();

		return "Done";
	}

	@RequestMapping("/delete")
	public String deleteById(@RequestParam("id") Long id) {
		customerRepository.delete(id);

		// Prometheus - tell opp antall requests
		httpRequestsTotal.labels("/delete-id").inc();

		return "Done";
	}

}