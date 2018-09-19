package no.nav.arena.nais.kafkaintegrasjon.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusConfiguration {
    @Bean
    public Counter counter() {
        return Counter
                .build("http_requests_total", "Total number of HTTP requests")
                .labelNames("path")
                .register();
    }

    @Bean
    public ServletRegistrationBean prometheusServlet() {
        return new ServletRegistrationBean(new MetricsServlet(), "/metrics/*");
    }
}
