package com.inditex.infrastructure.adapter.webclient;

import com.github.benmanes.caffeine.cache.Cache;
import com.inditex.infrastructure.config.CacheProvider;
import com.inditex.infrastructure.model.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceClientTest {

    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    private WebClient.RequestBodySpec requestBodySpecMock;
    private WebClient.ResponseSpec responseSpecMock;
    private WebClient webClientMock;
    private ProductServiceClient productServiceClient;

    private final String URL = "http://localhost/product";
    @Mock
    private Cache<String, ProductResponse> cacheMock;

    @BeforeEach
    void setupMocks() throws Exception {
        requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        requestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        responseSpecMock = mock(WebClient.ResponseSpec.class);
        webClientMock = mock(WebClient.class);

        new CacheProvider().init();
        productServiceClient = new ProductServiceClient(webClientMock);

        // Injecting the mock product URL using reflection
        Field productUrlField = ProductServiceClient.class.getDeclaredField("productUrl");
        productUrlField.setAccessible(true);
        productUrlField.set(productServiceClient, URL);
    }

    @Test
    @DisplayName("Should return a list of similar product IDs when the web client returns data")
    void shouldReturnSimilarProductIds_whenWebClientReturnsData() {
        // Arrange
        String productId = "1";
        List<String> expectedSimilarProductIds = List.of("2", "3", "4");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(URL + "/1/similarids")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(expectedSimilarProductIds));

        // Act
        Mono<List<String>> result = productServiceClient.getSimilarProductsByProductId(productId);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedSimilarProductIds)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should handle error and complete when web client returns an error")
    void shouldHandleError_whenWebClientReturnsError() {
        // Arrange
        String productId = "1";

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(URL + "/1/similarids")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new Exception("Error fetching similar product IDs")));

        // Act
        Mono<List<String>> result = productServiceClient.getSimilarProductsByProductId(productId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return product details with fallback when remote call times out")
    void shouldReturnProductDetailWithFallback_whenRemoteCallTimesOut() {
        // Arrange
        String productId = "1";
        ProductResponse expectedProductResponse = new ProductResponse("1", "Product 1", BigDecimal.ZERO, false);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(URL +"/1")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ProductResponse.class))
                .thenReturn(Mono.just(expectedProductResponse));

        // Act
        Mono<ProductResponse> result = productServiceClient.getProductDetailByProductId(productId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(product -> product.name().equals(expectedProductResponse.name()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return product details with delay and fallback when remote call times out")
    void shouldReturnProductDetailWithDelayAndFallback_whenRemoteCallTimesOut() {
        // Arrange
        String productId = "1";
        ProductResponse expectedProductResponse = new ProductResponse("0", "Fallback Product", BigDecimal.ZERO, false);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(URL + "/1")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ProductResponse.class))
                .thenReturn(Mono.just(expectedProductResponse).delayElement(Duration.ofSeconds(5)));

        // Act
        Mono<ProductResponse> result = productServiceClient.getProductDetailByProductId(productId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(product -> product.id().equals(expectedProductResponse.id()))
                .verifyComplete();
    }
}
