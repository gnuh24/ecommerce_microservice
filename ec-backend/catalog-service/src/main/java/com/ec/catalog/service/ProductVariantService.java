package com.ec.catalog.service;

import java.math.BigDecimal;
import java.util.List;
import com.ec.catalog.entity.ProductVariant;

public interface ProductVariantService {

    List<ProductVariant> getVariantsByProductId(String productId);

    ProductVariant getThumbnailVariant(String productId); // nếu có dùng

    BigDecimal getMinPriceByProductId(String productId);

    BigDecimal getMaxPriceByProductId(String productId);
}
