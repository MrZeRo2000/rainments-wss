package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractEntityService<Product, ProductRepository> {
    public ProductService(ProductRepository repository) {
        super(repository);
    }
}
