package com.ec.catalog.dto.productImage;

import lombok.Data;

@Data
public class ProductImageResponseDTO {
    private String id;
    private String imageUrl;
    private Boolean isThumbnail;
}