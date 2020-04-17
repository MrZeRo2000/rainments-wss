package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.ProductDTO;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;

import com.romanpulov.rainmentswss.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController extends BaseServiceRestController<Product, ProductDTO> {
    public ProductController(ProductService productService, EntityDTOMapper<Product, ProductDTO> mapper) {
        super(productService, mapper, LoggerFactory.getLogger(ProductController.class));
    }
}
