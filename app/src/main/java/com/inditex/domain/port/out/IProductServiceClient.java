package com.inditex.domain.port.out;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.infrastructure.model.ProductResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * External service for retrieving product data.
 */
public interface IProductServiceClient {

    /**
     * Gets similar product IDs by product ID.
     *
     * @param productId the product ID
     * @return a {@link Mono} of a list of product IDs
     */
    Mono<List<String>> getSimilarProductsByProductId(String productId);

    /**
     * Gets product detail by product ID.
     *
     * @param productId the product ID
     * @return a {@link Mono} of {@link ProductResponse}
     */
    Mono<ProductResponse> getProductDetailByProductId(String productId);
}
