package com.ec.catalog.dto.productImage;

import com.ec.catalog.entity.ProductImage;
import lombok.Builder;
import lombok.Data;

@Data
public class ProductImageResponseDTO {

    private String id;
    private String imageUrl;
    private Boolean isThumbnail;

    public ProductImageResponseDTO() {
    }

    public ProductImageResponseDTO(String id, String imageUrl, Boolean isThumbnail) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail;
    }

    public static ProductImageResponseDTO fromEntity(ProductImage image) {
        return new ProductImageResponseDTO(image.getId(), image.getImageUrl(), image.getIsThumbnail());
    }
}
