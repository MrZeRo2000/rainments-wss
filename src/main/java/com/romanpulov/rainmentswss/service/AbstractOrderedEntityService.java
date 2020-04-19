package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.entity.OrderedEntity;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;
import com.romanpulov.rainmentswss.repository.CustomQueryRepository;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractOrderedEntityService
        <E extends CommonEntity & OrderedEntity,
        R extends CrudRepository<E, Long>>
        extends AbstractEntityService<E, R>
{
    private final CustomQueryRepository customQueryRepository;

    private String getEntityTableName(E entity) {
        return entity.getClass().getSimpleName();
    }

    public AbstractOrderedEntityService(R repository, CustomQueryRepository customQueryRepository) {
        super(repository);
        this.customQueryRepository = customQueryRepository;
    }

    @Override
    protected void beforeEntityInsert(E entity) {
        Long maxOrderId = customQueryRepository.getMaxOrderId(getEntityTableName(entity));
        long orderId;
        if (maxOrderId == null) {
            orderId = 1L;
        } else {
            orderId = maxOrderId + 1L;
        }
        entity.setOrderId(orderId);

        super.beforeEntityInsert(entity);
    }

    @Override
    protected void beforeEntityUpdate(E entity, E savedEntity) {
        super.beforeEntityUpdate(entity, savedEntity);
        entity.setOrderId(savedEntity.getOrderId());
    }

    public void moveOrder(Long fromId, Long toId) throws CommonEntityNotFoundException {
        E fromEntity = getEntityById(fromId);
        E toEntity = getEntityById(toId);

        if ((fromEntity.getOrderId() == null) || (toEntity.getOrderId() == null)) {
            customQueryRepository.setDefaultOrder(getEntityTableName(fromEntity));
        }

        customQueryRepository.moveOrder(
                getEntityTableName(fromEntity),
                fromEntity.getId(),
                fromEntity.getOrderId(),
                toEntity.getId(),
                toEntity.getOrderId()
        );
    }
}
