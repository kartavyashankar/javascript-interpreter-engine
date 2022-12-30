package com.openide.javascriptinterpreterengine.controller;

import com.github.dockerjava.api.exception.DockerException;
import com.openide.javascriptinterpreterengine.model.code.response.CodeRunResponseModel;
import com.openide.javascriptinterpreterengine.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class MainController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CodeRunResponseModel> handleException(Exception ex) {
        return ResponseBuilder.buildError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DockerException.class)
    public ResponseEntity<CodeRunResponseModel> handleDockerException(DockerException ex) {
        System.out.println(ex.getMessage());
        return ResponseBuilder.buildError("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

