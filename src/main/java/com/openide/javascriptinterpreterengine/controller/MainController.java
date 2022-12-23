package com.openide.javascriptinterpreterengine.controller;

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

}

