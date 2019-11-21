package com.myBusApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidRelationalOperatorException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public InvalidRelationalOperatorException(String message)
    {
        super(message);
    }

}
