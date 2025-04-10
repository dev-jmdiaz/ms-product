package com.inditex.infrastructure.adapter.webclient;


import com.inditex.domain.port.out.IProductServiceClient;
import com.inditex.infrastructure.config.CacheProvider;
import com.inditex.infrastructure.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;


/**
 * Adapter for calling external product services.
 * <p>
 * Implements {@link IProductServiceClient} using WebClient to fetch
 * similar product IDs and product details.
 * </p>
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceClient implements IProductServiceClient {

    private final WebClient webClient;

    @Value("${resilience4j.circuitbreaker.timeoutDuration}")
    private int timeOut;
    @Value("${product.url}")
    private String productUrl;

    /**
     * Fetches similar product IDs from the external service.
     */

    @Override
    public Mono<List<String>> getSimilarProductsByProductId(String productId) {

        return webClient.get()
                .uri(String.format( "%s/%s/similarids",productUrl,productId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .doOnNext(d -> log.info("print data  productId {} {}",productId, d))
                .timeout(Duration.ofSeconds(timeOut))
                .onErrorResume(error -> {
                    log.error("Error al obtener el producto {}: {}", productId, error.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Fetches product details from the external service, with caching and fallback.
     */
    @Override
    public Mono<ProductResponse> getProductDetailByProductId(String productId) {

        ProductResponse cached = CacheProvider.getCache().getIfPresent(productId);
        if (Objects.nonNull(cached)) {
            return Mono.just(cached);
        }

        Mono<ProductResponse> remoteCall = webClient.get()
                .uri(String.format("%s/%s",productUrl, productId))
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .doOnNext(data -> {
                    log.info("Cargando en caché productoId={}", productId);
                    CacheProvider.getCache().put(productId, data);
                })
                .doOnError(error -> log.error("Error al obtener productoId={} -> {}", productId, error.getMessage()))
                .cache();

        remoteCall.subscribeOn(Schedulers.boundedElastic()).subscribe();

        return remoteCall
                .timeout(Duration.ofSeconds(timeOut))
                .onErrorResume(TimeoutException.class, e -> fallbackGetProductDetail());
    }


    private Mono<ProductResponse> fallbackGetProductDetail() {
        return Mono.just(new ProductResponse("0","", BigDecimal.ZERO, Boolean.FALSE));
    }

}