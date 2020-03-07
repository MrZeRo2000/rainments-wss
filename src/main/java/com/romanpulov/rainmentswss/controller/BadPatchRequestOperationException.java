package com.romanpulov.rainmentswss.controller;

public class BadPatchRequestOperationException extends Exception {
    public BadPatchRequestOperationException(String patchRequestOperation) {
        super(String.format("Bad patch request operation: %s", patchRequestOperation));
    }
}
