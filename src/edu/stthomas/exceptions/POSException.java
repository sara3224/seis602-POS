package edu.stthomas.exceptions;

public class POSException extends Exception {
    private String message;
    public POSException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
