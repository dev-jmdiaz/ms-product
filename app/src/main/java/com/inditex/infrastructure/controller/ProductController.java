package com.inditex.infrastructure.controller;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.application.caseuse.GetProductDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * REST controller for handling product-related requests.
 * <p>
 * This controller exposes endpoints related to product operations,
 * such as retrieving similar product details.
 * </p>
 *
 * @author Jhonatan Mallqui
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final GetProductDetailsUseCase getProductDetails;

    /**
     * Retrieves similar products based on the given product ID.
     *
     * @param productId the ID of the product to find similar products for
     * @param exchange  the current server web exchange
     * @return a {@link Mono} emitting a {@link ResponseEntity} containing a {@link Flux} of {@link ProductDetailDTO}
     */
    @Override
    public Mono<ResponseEntity<Flux<ProductDetailDTO>>> getProductSimilar(String productId, ServerWebExchange exchange) {

        Flux<ProductDetailDTO> productDetails = getProductDetails.getProductDetails(productId);
        return Mono.just(ResponseEntity.ok(productDetails));
    }
}
