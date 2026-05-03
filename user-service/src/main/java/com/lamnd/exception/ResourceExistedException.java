package com.lamnd.exception;

public class ResourceExistedException extends RuntimeException{
    String resourceName;
    String fieldName;
    Object fieldValue;

    public ResourceExistedException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
