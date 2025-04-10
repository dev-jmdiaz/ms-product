package com.inditex.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



/**
 * Configuration class for WebClient, CircuitBreaker, and TimeLimiter beans.
 * <p>
 * This configuration sets up the WebClient for making HTTP requests, along with
 * a CircuitBreaker and TimeLimiter for handling resilience and timeouts.
 * </p>
 */
@Configuration
public class WebConfig {

    /**
     * Creates and provides a {@link WebClient.Builder} bean.
     *
     * @return a new {@link WebClient.Builder}
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }


    /**
     * Creates and provides a {@link CircuitBreaker} bean for handling circuit breaking
     * in the product details service.
     *
     * @param registry the {@link CircuitBreakerRegistry} to register the circuit breaker
     * @return a {@link CircuitBreaker} instance
     */
    @Bean
    public CircuitBreaker productDetailsCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("productDetails");
    }

    /**
     * Creates and provides a {@link TimeLimiter} bean for handling timeouts in the product detail service.
     *
     * @return a {@link TimeLimiter} instance
     */

    @Bean
    public TimeLimiter productDetailTimeLimiter() {
        return TimeLimiter.ofDefaults("productDetailTimeLimiter");
    }
}
