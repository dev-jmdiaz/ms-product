package com.inditex.application.caseuse;

import com.inditex.application.dto.ProductDetailDTO;
import reactor.core.publisher.Flux;

/**
 * Defines the use case for fetching similar product details.
 */
public interface GetProductDetailsUseCase {

    /**
     * Gets similar product details by product ID.
     *
     * @param productId the product ID
     * @return a {@link Flux} of {@link ProductDetailDTO}
     */
    Flux<ProductDetailDTO> getProductDetails(String productId);
}
