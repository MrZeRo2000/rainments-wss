package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractEntityService<E extends CommonEntity, R extends CrudRepository<E, Long>> implements EntityService<E> {

    protected final R repository;

    public AbstractEntityService(R repository) {
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
    public <S extends E> S update(Long id, S entity) throws CommonEntityNotFoundException {
        E storedEntity = repository.findById(id).orElseThrow(() -> new CommonEntityNotFoundException(id));
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) throws CommonEntityNotFoundException
    {
        E entity = repository.findById(id).orElseThrow(() -> new CommonEntityNotFoundException(id));
        repository.delete(entity);
    }
}
