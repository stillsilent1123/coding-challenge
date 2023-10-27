package com.mindex.challenge.exceptions;

//Created Custom Exceptions in case future use cases were necessary for handling these in a specific way. (Replaced Generic Runtime Exception)
public class DataValidationException extends RuntimeException{
    public DataValidationException(String message){super(message);}
}
