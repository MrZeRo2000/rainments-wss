package com.romanpulov.rainmentswss.exception;

import com.romanpulov.rainmentswss.exception.NotFoundException;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException(Long id) {
        super(String.format("Entity with id=%d not found", id));
    }
}
