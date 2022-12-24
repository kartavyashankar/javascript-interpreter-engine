package com.openide.javascriptinterpreterengine.controller.code;

import com.openide.javascriptinterpreterengine.controller.MainController;
import com.openide.javascriptinterpreterengine.model.code.request.CodeRunRequestModel;
import com.openide.javascriptinterpreterengine.model.code.response.CodeRunResponseModel;
import com.openide.javascriptinterpreterengine.service.code.CodeService;
import com.openide.javascriptinterpreterengine.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/code")
public class CodeController extends MainController {
    @Autowired
    @Qualifier("code-impl")
    private CodeService codeService;

    @PostMapping("execute")
    public ResponseEntity<CodeRunResponseModel> executeCode(@RequestBody CodeRunRequestModel codeRunRequest) {
        String output = codeService.runCode(codeRunRequest.getCode());
        return ResponseBuilder.buildResponse(output, HttpStatus.OK);
    }
}
