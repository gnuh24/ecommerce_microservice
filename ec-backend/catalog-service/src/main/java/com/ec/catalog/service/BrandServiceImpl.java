package com.ec.catalog.service;

import com.ec.catalog.entity.Brand;
import com.ec.catalog.repository.BrandRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Brand> getAllBrandNoPaging() {
        return brandRepository.findAllByIsDeletedFalse();
    }

    // Các method CRUD có thể viết tương tự Category nếu cần

    // private void validateBrandName(String name, String currentId) throws Exception {
    //     Optional<Brand> existing = brandRepository.findByBrandName(name);
    //     if (existing.isPresent() && !existing.get().getId().equals(currentId)) {
    //         throw new Exception("Tên thương hiệu '" + name + "' đã tồn tại!");
    //     }
    // }
}
