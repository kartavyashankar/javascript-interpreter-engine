package com.openide.javascriptinterpreterengine.service.code.impl;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.openide.javascriptinterpreterengine.service.code.CodeService;
import com.openide.javascriptinterpreterengine.utils.DockerHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("code-impl")
public class CodeServiceImpl implements CodeService {
    @Override
    public String runCode(String code) {
        ArrayList<String> codeLines = new ArrayList<>(List.of(code.split("\n")));
        InspectContainerResponse container = DockerHandler.createContainer("node:18-alpine");
        container = DockerHandler.startContainer(container);
        return null;
    }
}
