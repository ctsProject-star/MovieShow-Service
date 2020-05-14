package com.cts.project.movieshow.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieShowNotFoundException extends RuntimeException 
{
	private static final long serialVersionUID = 100L;

	public MovieShowNotFoundException() 
    {
        super();
    }

    public MovieShowNotFoundException(String message) 
    {
        super(message);
    }
    
    public MovieShowNotFoundException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}
