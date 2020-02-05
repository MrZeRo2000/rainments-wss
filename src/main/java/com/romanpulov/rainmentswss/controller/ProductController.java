package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.ProductDTO;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;

import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController extends BaseRestController<Product, ProductDTO> {
    public ProductController(CrudRepository<Product, Long> repository, EntityDTOMapper<Product, ProductDTO> mapper) {
        super(repository, mapper, LoggerFactory.getLogger(ProductController.class));
    }
}
