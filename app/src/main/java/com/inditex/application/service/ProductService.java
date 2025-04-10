package com.inditex.application.service;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.application.caseuse.GetProductDetailsUseCase;
import com.inditex.application.mapper.ProductDtoMapper;
import com.inditex.domain.port.out.IProductServiceClient;
import com.inditex.domain.exception.NotFoundException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Service that implements the use case to retrieve similar product details.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements GetProductDetailsUseCase {

    private final IProductServiceClient productExternalService;

    private final CircuitBreaker productDetailsCircuitBreaker;

    private final ProductDtoMapper mapper;

    /**
     * Retrieves details for products similar to the given product ID.
     *
     * @param productId the product ID
     * @return a {@link Flux} of {@link ProductDetailDTO}
     */
    @Override
    public Flux<ProductDetailDTO> getProductDetails(String productId) {

        return productExternalService.getSimilarProductsByProductId(productId)
                .transformDeferred(CircuitBreakerOperator.of(productDetailsCircuitBreaker))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.parallel())
                .flatMap(similarId -> productExternalService.getProductDetailByProductId(similarId)
                        .onErrorResume(error -> {
                            log.error("Error al obtener detalles para el id: {}", similarId, error);
                            return Mono.empty();
                        }))
                .switchIfEmpty(Flux.defer(() -> {
                    log.warn("No se encontraron productos similares para el producto {}", productId);
                    return Flux.error(new NotFoundException("No se encontraron productos"));
                })).map(mapper::fromDomain);
    }
}
