package com.click.auth.exception;

public class NotFoundExcetion extends IllegalArgumentException{
    public NotFoundExcetion(String target){
        super(target);
    }
}
