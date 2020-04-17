package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;

import java.util.Optional;

public interface EntityService<E extends CommonEntity> {
    Iterable<E> findAll();
    <S extends E> S save(S entity);
    Optional<E> findById(Long id);
    void deleteById(Long id);
}

