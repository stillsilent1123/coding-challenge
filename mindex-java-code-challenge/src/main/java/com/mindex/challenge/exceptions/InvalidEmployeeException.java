package com.mindex.challenge.exceptions;

//Created Custom Exceptions in case future use cases were necessary for handling these in a specific way. (Replaced Generic Runtime Exception)
public class InvalidEmployeeException extends RuntimeException{
    public InvalidEmployeeException (String message){super(message);}
}
