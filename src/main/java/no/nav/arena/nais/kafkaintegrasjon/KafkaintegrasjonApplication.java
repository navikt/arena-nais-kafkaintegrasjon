package no.nav.arena.nais.kafkaintegrasjon;

import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class KafkaintegrasjonApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaintegrasjonApplication.class, args);
    }

    // Prometheus
    @Bean
    ServletRegistrationBean createPrometheusServlet(){
    return new ServletRegistrationBean(new MetricsServlet(), "/metrics/*");
    }
}
