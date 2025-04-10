package com.inditex.application.mapper;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.infrastructure.model.ProductResponse;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between domain models and Data Transfer Objects (DTOs)
 * related to products.
 * <p>
 * This interface is implemented automatically by MapStruct at compile time.
 * </p>
 *
 * <p>
 * The {@code componentModel = "spring"} setting allows Spring to detect and inject this mapper
 * as a bean.
 * </p>
 *
 * @author Jhonatan Mallqui
 */
@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    /**
     * Converts a {@link ProductResponse} object to a {@link ProductDetailDTO} object.
     *
     * @param response The {@link ProductResponse} object containing the product data.
     * @return A {@link ProductDetailDTO} object representing the detailed product information.
     */
    ProductDetailDTO fromDomain(ProductResponse response);
}
