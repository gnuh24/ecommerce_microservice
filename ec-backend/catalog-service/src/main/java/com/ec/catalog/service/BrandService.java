package com.ec.catalog.service;

//import com.ec.catalog.dto.brand.BrandCreateForm;
//import com.ec.catalog.dto.brand.BrandUpdateForm;
import com.ec.catalog.entity.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> getAllBrandNoPaging();

    // Uncomment if needed
    // Page<Brand> getAllBrand(Pageable pageable, String search);
    // Brand getBrandById(String id);
    // Brand createBrand(BrandCreateForm form) throws Exception;
    // Brand updateBrand(String id, BrandUpdateForm form) throws Exception;
    // void deleteBrand(String id) throws Exception;
}
