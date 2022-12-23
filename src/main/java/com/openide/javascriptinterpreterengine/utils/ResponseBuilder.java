package com.openide.javascriptinterpreterengine.utils;

import com.openide.javascriptinterpreterengine.model.code.response.CodeRunResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseBuilder {

    public static ResponseEntity<CodeRunResponseModel> buildResponse(String output, HttpStatus statusCode) {
        CodeRunResponseModel response = new CodeRunResponseModel();
        response.setOutput(output);
        response.setCode(statusCode.value());
        return new ResponseEntity<>(response, statusCode);
    }

    public static ResponseEntity<CodeRunResponseModel> buildError(String errorMessage, HttpStatus statusCode) {
        CodeRunResponseModel response = new CodeRunResponseModel();
        response.setError(errorMessage);
        response.setCode(statusCode.value());
        return new ResponseEntity<>(response, statusCode);
    }

}
