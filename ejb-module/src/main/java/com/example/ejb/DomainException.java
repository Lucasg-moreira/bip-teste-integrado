package com.example.ejb;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
