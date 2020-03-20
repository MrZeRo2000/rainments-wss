package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    List<Product> findAllByOrderByIdAsc();
    List<Product> findByName(String name);
}

