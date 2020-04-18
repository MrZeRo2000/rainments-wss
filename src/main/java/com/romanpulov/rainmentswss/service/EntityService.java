package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;

import java.util.Optional;

public interface EntityService<E extends CommonEntity> {
    Iterable<E> findAll();
    <S extends E> S save(S entity) ;
    <S extends E> S update(Long id, S entity) throws CommonEntityNotFoundException;
    void deleteById(Long id) throws CommonEntityNotFoundException;
}

