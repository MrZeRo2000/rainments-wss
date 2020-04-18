package com.romanpulov.rainmentswss.exception;

import com.romanpulov.rainmentswss.exception.NotFoundException;

public class CommonEntityNotFoundException extends NotFoundException {
    public CommonEntityNotFoundException(Long id) {
        super(String.format("Entity with id=%d not found", id));
    }
}
