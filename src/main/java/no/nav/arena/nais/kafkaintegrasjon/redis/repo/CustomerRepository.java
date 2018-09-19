package no.nav.arena.nais.kafkaintegrasjon.redis.repo;


import java.util.Map;

import no.nav.arena.nais.kafkaintegrasjon.redis.model.Customer;
import org.springframework.stereotype.Repository;

public interface CustomerRepository {

	void save(Customer customer);
	Customer find(Long id);
	Map<Long, Customer> findAll();
	void update(Customer customer);
	void delete(Long id);

}