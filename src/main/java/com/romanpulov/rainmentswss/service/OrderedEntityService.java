package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;

public interface OrderedEntityService<E extends CommonEntity> extends EntityService<E> {
    int moveOrder(Long fromId, Long toId) throws CommonEntityNotFoundException;
    int setDefaultOrder(Class<?> entityClass);
}
