package com.ecommerce.project.exceptions;

public class APIException  extends  RuntimeException{

    String message;
    private static final long serialVersionUID = 1L;

    public APIException(){

    }

    public APIException(String message) {
        super(message);
        this.message = message;

    }
}
