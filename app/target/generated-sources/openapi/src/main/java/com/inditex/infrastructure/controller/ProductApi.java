/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.inditex.infrastructure.controller;

import com.inditex.application.dto.ProductDetailDTO;
import java.util.Set;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.Part;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-04-10T13:34:04.373662+02:00[Europe/Madrid]")
@Validated
@Tag(name = "Product", description = "Operations related to products")
public interface ProductApi {

    /**
     * GET /product/{productId}/similar : Similar products
     *
     * @param productId  (required)
     * @return OK (status code 200)
     *         or Product Not found (status code 404)
     */
    @Operation(
        operationId = "getProductSimilar",
        summary = "Similar products",
        tags = { "Product" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDetailDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Product Not found")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/product/{productId}/similar",
        produces = { "application/json" }
    )
    default Mono<ResponseEntity<Flux<ProductDetailDTO>>> getProductSimilar(
        @Parameter(name = "productId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("productId") String productId,
        @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(Mono.empty());

    }

}
