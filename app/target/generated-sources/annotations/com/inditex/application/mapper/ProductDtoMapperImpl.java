package com.inditex.application.mapper;

import com.inditex.application.dto.ProductDetailDTO;
import com.inditex.infrastructure.model.ProductResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-10T13:34:06+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class ProductDtoMapperImpl implements ProductDtoMapper {

    @Override
    public ProductDetailDTO fromDomain(ProductResponse response) {
        if ( response == null ) {
            return null;
        }

        ProductDetailDTO productDetailDTO = new ProductDetailDTO();

        productDetailDTO.setId( response.id() );
        productDetailDTO.setName( response.name() );
        productDetailDTO.setPrice( response.price() );
        productDetailDTO.setAvailability( response.availability() );

        return productDetailDTO;
    }
}
