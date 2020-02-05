package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.entity.CommonEntity;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseRestController<E extends CommonEntity, D> {
    protected final Logger logger;

    protected final CrudRepository<E, Long> repository;
    protected final EntityDTOMapper<E, D> mapper;

    public BaseRestController(CrudRepository<E, Long> repository, EntityDTOMapper<E, D> mapper, Logger logger) {
        this.repository = repository;
        this.mapper = mapper;
        this.logger = logger;
    }

    @GetMapping
    ResponseEntity<List<D>> all() {
        List<D> result = new ArrayList<>();
        this.repository.findAll().forEach(paymentObject -> result.add(mapper.entityToDTO(paymentObject)));

        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<D> post(@RequestBody D dto) {
        E entity = mapper.dtoTOEntity(dto);
        E newEntity = repository.save(entity);
        return ResponseEntity.ok(mapper.entityToDTO(newEntity));
    }

    @PutMapping("/{id}")
    ResponseEntity<D> put(@PathVariable Long id, @RequestBody D dto) {
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            E updatedEntity = mapper.dtoTOEntity(dto);
            updatedEntity.setId(id);
            E newPaymentObject = repository.save(updatedEntity);
            return ResponseEntity.ok(mapper.entityToDTO(newPaymentObject));
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
    }

}
