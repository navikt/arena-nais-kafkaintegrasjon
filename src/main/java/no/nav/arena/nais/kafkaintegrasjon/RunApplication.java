package no.nav.arena.nais.kafkaintegrasjon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

@SpringBootApplication
public class RunApplication {

    @Autowired
    public ServletRegistrationBean prometheusServlet;

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }
}
