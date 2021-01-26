package com.example.demo.exception;

import java.util.function.Supplier;

public class NotFoundException extends Exception {
    public NotFoundException(){
        super("User not found");
    }

    public NotFoundException(String message){
        super(message);
    }
}
