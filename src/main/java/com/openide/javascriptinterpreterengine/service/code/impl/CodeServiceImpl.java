package com.openide.javascriptinterpreterengine.service.code.impl;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.openide.javascriptinterpreterengine.service.code.CodeService;
import com.openide.javascriptinterpreterengine.utils.DockerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("code-impl")
public class CodeServiceImpl implements CodeService {
    @Autowired
    @Qualifier("docker-handler")
    private DockerHandler dockerHandler;

    @Override
    public String runCode(String code) throws InterruptedException, DockerException {
        InspectContainerResponse container = dockerHandler.createContainer("kartavyashankar/js-engine:v1.0");
        container = dockerHandler.startContainer(container);
        return container.getId();
    }
}
