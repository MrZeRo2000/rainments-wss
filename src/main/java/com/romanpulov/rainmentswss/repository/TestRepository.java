package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestEntity, Long> {
}
