package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.TestEntity;
import com.romanpulov.rainmentswss.repository.TestRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RepositoryTestEntityTests {

    private static final Logger log = Logger.getLogger(RepositoryTestEntityTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private TestRepository testRepository;

    @Test
    void mainTest() {
        TestEntity newEntity = new TestEntity();
        newEntity.setTestDate(LocalDate.now());
        testRepository.save(newEntity);

        Iterable<TestEntity> testEntities = testRepository.findAll();
        assertThat(testEntities).isNotNull();

        TestEntity savedEntity = testEntities.iterator().next();

        assertThat(savedEntity.getTestDate()).isEqualTo(newEntity.getTestDate());

        testRepository.deleteAll();

        testEntities = testRepository.findAll();
        assertThat(testEntities.iterator().hasNext()).isFalse();
    }

}
