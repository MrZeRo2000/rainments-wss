package com.romanpulov.rainmentswss.entitymapper;

import com.romanpulov.rainmentswss.dto.ProductDTO;
import com.romanpulov.rainmentswss.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOMapper implements EntityDTOMapper<Product, ProductDTO> {
    @Override
    public ProductDTO entityToDTO(Product entity) {
        return new ProductDTO(entity.getId(), entity.getName(), entity.getUnitName(), entity.getCounterPrecision());
    }

    @Override
    public Product dtoTOEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUnitName(dto.getUnitName());
        entity.setCounterPrecision(dto.getCounterPrecision());

        return entity;
    }

    @Override
    public Class<?> getEntityClass() {
        return Product.class;
    }
}
