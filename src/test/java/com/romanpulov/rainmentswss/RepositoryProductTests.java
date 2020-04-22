package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryProductTests {

    private static final Logger log = Logger.getLogger(RepositoryProductTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void mainTest() {
        Iterable<Product> products;

        products = productRepository.findAllByOrderByOrderIdAsc();
        assertThat(products).isNotNull();
    }
}
