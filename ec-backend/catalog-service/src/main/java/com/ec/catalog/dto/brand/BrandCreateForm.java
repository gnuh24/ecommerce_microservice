package com.ec.catalog.dto.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandCreateForm {

    @NotBlank(message = "Tên thương hiệu không được để trống")
    private String name;
}
