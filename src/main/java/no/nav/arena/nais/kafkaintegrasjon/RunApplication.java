package no.nav.arena.nais.kafkaintegrasjon;

import no.nav.arena.nais.kafkaintegrasjon.kafka.KafkaEmbeddedServer;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class RunApplication {


    public static void main(String[] args) {
        KafkaEmbeddedServer.startEmbeddedKafka();
        SpringApplication.run(RunApplication.class, args);
    }

    // Prometheus
    @Bean
    ServletRegistrationBean createPrometheusServlet(){
    return new ServletRegistrationBean(new MetricsServlet(), "/metrics/*");
    }
}
