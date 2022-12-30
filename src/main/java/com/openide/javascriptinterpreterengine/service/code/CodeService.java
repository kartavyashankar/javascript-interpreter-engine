package com.openide.javascriptinterpreterengine.service.code;

import com.github.dockerjava.api.exception.DockerException;

public interface CodeService {
    String runCode(String code) throws InterruptedException, DockerException;
}
