package com.inditex.infrastructure.model;

import java.math.BigDecimal;

/**
 *  Model representing a product.
 *
 * @param id           the product ID
 * @param name         the product name
 * @param price        the product price
 * @param availability whether the product is available
 */
public record ProductResponse(String id, String name, BigDecimal price, Boolean availability){}