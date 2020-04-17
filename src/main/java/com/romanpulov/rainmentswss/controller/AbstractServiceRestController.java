package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.service.EntityService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractServiceRestController<E extends CommonEntity, D, S extends EntityService<E>> {

    protected final S entityService;
    protected final EntityDTOMapper<E, D> mapper;
    protected final Logger logger;

    public AbstractServiceRestController(S entityService, EntityDTOMapper<E, D> mapper, Logger logger) {
        this.entityService = entityService;
        this.mapper = mapper;
        this.logger = logger;
    }

    @GetMapping
    ResponseEntity<List<D>> all() {
        List<D> result = new ArrayList<>();
        this.entityService.findAll().forEach(paymentObject -> result.add(mapper.entityToDTO(paymentObject)));

        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<D> post(@RequestBody D dto) {
        E entity = mapper.dtoTOEntity(dto);
        E newEntity = entityService.save(entity);
        return ResponseEntity.ok(mapper.entityToDTO(newEntity));
    }

    @PutMapping("/{id}")
    ResponseEntity<D> put(@PathVariable Long id, @RequestBody D dto) throws EntityNotFoundException {
        Optional<E> entity = entityService.findById(id);
        if (entity.isPresent()) {
            E updatedEntity = mapper.dtoTOEntity(dto);
            updatedEntity.setId(id);
            E newPaymentObject = entityService.save(updatedEntity);
            return ResponseEntity.ok(mapper.entityToDTO(newPaymentObject));
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            throw new EntityNotFoundException(id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws EntityNotFoundException {
        if (entityService.findById(id).isPresent()) {
            entityService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            throw new EntityNotFoundException(id);
        }
    }
}
