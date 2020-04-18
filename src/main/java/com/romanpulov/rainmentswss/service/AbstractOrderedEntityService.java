package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.entity.OrderedEntity;
import com.romanpulov.rainmentswss.repository.CustomQueryRepository;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractOrderedEntityService
        <E extends CommonEntity & OrderedEntity,
        R extends CrudRepository<E, Long>>
        extends AbstractEntityService<E, R>
{
    private final CustomQueryRepository customQueryRepository;

    public AbstractOrderedEntityService(R repository, CustomQueryRepository customQueryRepository) {
        super(repository);
        this.customQueryRepository = customQueryRepository;
    }

    @Override
    public <S extends E> S save(S entity) {
        Long maxOrderId = customQueryRepository.getMaxOrderId(entity.getClass().getSimpleName());
        long orderId;
        if (maxOrderId == null) {
            orderId = 1L;
        } else {
            orderId = maxOrderId + 1L;
        }
        entity.setOrderId(orderId);
        return super.save(entity);
    }
}
