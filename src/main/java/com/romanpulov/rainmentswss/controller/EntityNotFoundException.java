package com.romanpulov.rainmentswss.controller;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException(Long id) {
        super(String.format("Entity with id=%d not found", id));
    }
}
