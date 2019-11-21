package com.myBusApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class DriverProfileDeactivatedException extends Exception{
	
	static final long serialVersionUID = -3387516993224229948L;


    public DriverProfileDeactivatedException(String message)
    {
        super(message);
    }

}
