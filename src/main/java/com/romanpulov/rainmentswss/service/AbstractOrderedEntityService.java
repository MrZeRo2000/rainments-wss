package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.entity.OrderedEntity;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;
import com.romanpulov.rainmentswss.repository.CustomQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractOrderedEntityService
        <E extends CommonEntity & OrderedEntity,
        R extends CrudRepository<E, Long>>
        extends AbstractEntityService<E, R>
        implements OrderedEntityService<E>
{

    private final Logger logger = LoggerFactory.getLogger(AbstractOrderedEntityService.class.getName());

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

    @Transactional
    public int moveOrder(Long fromId, Long toId) throws CommonEntityNotFoundException {
        E fromEntity = getEntityById(fromId);
        E toEntity = getEntityById(toId);

        if ((fromEntity.getOrderId() == null) || (toEntity.getOrderId() == null)) {
            logger.info("Null order id, setting default order");

            customQueryRepository.setDefaultOrder(getEntityTableName(fromEntity));

            customQueryRepository.clearEntityManager();

            fromEntity = getEntityById(fromId);
            toEntity = getEntityById(toId);
            
            if ((fromEntity.getOrderId() == null) || (toEntity.getOrderId() == null)) {
                throw new RuntimeException("Error in setting default order");
            }
        }

        logger.info("FromEntity=" + fromEntity + ", ToEntity=" + toEntity);

        return customQueryRepository.moveOrder(
                getEntityTableName(fromEntity),
                fromEntity.getId(),
                fromEntity.getOrderId(),
                toEntity.getId(),
                toEntity.getOrderId()
        );
    }
}
