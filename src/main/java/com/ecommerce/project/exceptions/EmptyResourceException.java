package com.ecommerce.project.exceptions;

public class EmptyResourceException extends RuntimeException {
    String resourceName;
    String property;
    String value;
    String keyword;
    public EmptyResourceException(String resourceName) {
        super(String.format("No %s added so far", resourceName));
        this.resourceName = resourceName;
    }

    public EmptyResourceException(String resourceName, String property, String value) {
        super(String.format("No %s having %s : %s added so far", resourceName,property,value));
        this.resourceName = resourceName;
        this.property = property;
        this.value = value;
    }

    public EmptyResourceException(String resourceName, String keyword) {
        super(String.format("No %s found matching %s", resourceName,keyword));
        this.resourceName = resourceName;
        this.keyword = keyword;
    }
}
