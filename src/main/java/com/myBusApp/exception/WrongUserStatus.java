package com.myBusApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class WrongUserStatus extends Exception {
	
	static final long serialVersionUID = -3387516993334229948L;


    public WrongUserStatus(String message)
    {
        super(message);
    }

	
	
}
