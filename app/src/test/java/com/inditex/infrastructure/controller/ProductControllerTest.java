package com.inditex.infrastructure.controller;

import com.inditex.application.caseuse.GetProductDetailsUseCase;
import com.inditex.application.dto.ProductDetailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {


    @InjectMocks
    private ProductController controller;
    @Mock
    private GetProductDetailsUseCase getProductDetails;



    @Test
    void testGetProductSimilar() {
        when(getProductDetails.getProductDetails(anyString()))
                .thenReturn(Flux.just(new ProductDetailDTO()));

        var mockServerWebExchange = mock(ServerWebExchange.class);
        var test = controller.getProductSimilar("1", mockServerWebExchange);

        StepVerifier.create(test)
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful())
                .verifyComplete();
    }

}
