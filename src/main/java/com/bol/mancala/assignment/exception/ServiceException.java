package com.bol.mancala.assignment.exception;

public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ServiceException(String message, Exception e) {
        super(message, e);
    }

    public ServiceException(String message) {
        super(message);
    }
}
