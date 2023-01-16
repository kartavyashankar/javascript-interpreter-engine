package com.openide.javascriptinterpreterengine.service.code.impl;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.openide.javascriptinterpreterengine.service.code.CodeService;
import com.openide.javascriptinterpreterengine.utils.DockerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("code-impl")
public class CodeServiceImpl implements CodeService {
    @Autowired
    @Qualifier("docker-handler")
    private DockerHandler dockerHandler;

    @Override
    public String runCode(String code) throws InterruptedException, DockerException {
        System.out.println(code);
        String[] codeLines = code.split("\n");
        ArrayList<String> commands = new ArrayList<>();
        for(int i=0; i < codeLines.length; i++) {
            commands.add("sh -c \"echo '" + codeLines[i] + "' >> code.js\"");
        }
        commands.add("sh -c \"node code.js\"");
        commands.forEach(System.out::println);
        InspectContainerResponse container = dockerHandler.createContainer("kartavyashankar/js-engine:v1.0");
        container = dockerHandler.startContainer(container);
        container = dockerHandler.stopContainer(container);
        dockerHandler.removeContainer(container);
        return container.getId();
    }
}
