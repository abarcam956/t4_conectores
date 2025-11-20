package com.edu;

public class DataAccessException extends RuntimeException {
    
    public DataAccessException(String msg){
        super(msg);
    }

    public DataAccessException(Throwable e){
        super(e);
    }

    public DataAccessException(String msg, Throwable e){
        super(msg, e);
    }
}
