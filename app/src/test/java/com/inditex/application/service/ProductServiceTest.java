package com.inditex.application.service;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.application.mapper.ProductDtoMapper;
import com.inditex.domain.port.out.IProductServiceClient;
import com.inditex.infrastructure.model.ProductResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductServiceClient productServiceClient;

    @Mock
    private ProductDtoMapper productDtoMapper;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("testCircuitBreaker");
        productService = new ProductService(productServiceClient, circuitBreaker, productDtoMapper);
    }

    @Test
    @DisplayName("Should return product details successfully with resilience handling")
    void shouldReturnProductDetailsSuccessfully_withResilienceHandling() {

        // Arrange
        String productId = "1";
        List<String> similarProductIds = List.of("1");
        ProductResponse productResponse = new ProductResponse(productId, "test product", BigDecimal.ZERO, false);
        ProductDetailDTO expectedProductDetailDTO = new ProductDetailDTO(productId, "test product", BigDecimal.ZERO, false);

        when(productDtoMapper.fromDomain(productResponse)).thenReturn(expectedProductDetailDTO);
        when(productServiceClient.getSimilarProductsByProductId(anyString())).thenReturn(Mono.just(similarProductIds));
        when(productServiceClient.getProductDetailByProductId(anyString())).thenReturn(Mono.just(productResponse));

        // Act
        Flux<ProductDetailDTO> productDetails = productService.getProductDetails(productId);

        // Assert
        StepVerifier.create(productDetails)
                .expectNext(expectedProductDetailDTO)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error message when product details fail to load due to external service failure")
    void shouldReturnErrorMessage_whenProductDetailsFail_dueToExternalServiceError() {

        // Arrange
        String productId = "1";
        List<String> similarProductIds = List.of("1");

        when(productServiceClient.getSimilarProductsByProductId(anyString())).thenReturn(Mono.just(similarProductIds));
        when(productServiceClient.getProductDetailByProductId(anyString())).thenReturn(Mono.error(new Exception("External service failed")));

        // Act
        Flux<ProductDetailDTO> productDetails = productService.getProductDetails(productId);

        // Assert
        StepVerifier.create(productDetails)
                .expectErrorMessage("No se encontraron productos")
                .verify();
    }

    @Test
    @DisplayName("Should return error message when no similar products are found")
    void shouldReturnErrorMessage_whenNoSimilarProductsFound() {

        // Arrange
        String productId = "1";
        when(productServiceClient.getSimilarProductsByProductId(anyString())).thenReturn(Mono.just(List.of()));

        // Act
        Flux<ProductDetailDTO> productDetails = productService.getProductDetails(productId);

        // Assert
        StepVerifier.create(productDetails)
                .expectErrorMessage("No se encontraron productos")
                .verify();
    }
}
