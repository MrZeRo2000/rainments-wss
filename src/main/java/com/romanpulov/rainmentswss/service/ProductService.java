package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.CustomQueryRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractOrderedEntityService<Product, ProductRepository> {
    public ProductService(ProductRepository repository, CustomQueryRepository customQueryRepository) {
        super(repository, customQueryRepository);
    }

    @Override
    public Iterable<Product> findAll() {
        return repository.findAllByOrderByOrderIdAsc();
    }
}
