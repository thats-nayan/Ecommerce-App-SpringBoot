package com.ecommerce.project.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;

    public ResourceAlreadyExistsException(String resourceName, String field, String fieldName) {
        super(String.format("%s already exists with %s: %s",resourceName, field , fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
}
