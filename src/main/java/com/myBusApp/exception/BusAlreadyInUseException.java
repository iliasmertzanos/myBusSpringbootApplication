package com.myBusApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusAlreadyInUseException extends Exception {
	
	static final long serialVersionUID = -3387516993334229948L;


    public BusAlreadyInUseException(String message)
    {
        super(message);
    }

	
	
}
