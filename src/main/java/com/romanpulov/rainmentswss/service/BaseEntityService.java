package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class BaseEntityService<E extends CommonEntity, R extends CrudRepository<E, Long>> implements EntityService<E> {

    protected final R repository;

    public BaseEntityService(R repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    @Override
    public <S extends E> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
