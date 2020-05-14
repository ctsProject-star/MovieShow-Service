package com.cts.project.movieshow.errorHandling;
 
 
public class CustomErrorType {
 
	private String errorCode;
    private String errorMessage;
    
    public CustomErrorType(String errorCode,String errorMessage)
    {
    	this.errorCode=errorCode;
        this.errorMessage = errorMessage;
    }
 
    public String getErrorCode() {
		return errorCode;
	}
    public String getErrorMessage() {
        return errorMessage;
    }

	
 
}