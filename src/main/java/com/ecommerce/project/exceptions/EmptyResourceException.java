package com.ecommerce.project.exceptions;

public class EmptyResourceException extends RuntimeException {
    String resourceName;
    public EmptyResourceException(String resourceName) {
        super(String.format("No data added in %s so far", resourceName));
        this.resourceName = resourceName;
    }
}
