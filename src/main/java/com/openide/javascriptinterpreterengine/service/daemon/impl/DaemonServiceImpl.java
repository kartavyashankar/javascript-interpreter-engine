package com.openide.javascriptinterpreterengine.service.daemon.impl;

import com.openide.javascriptinterpreterengine.service.daemon.DaemonService;
import com.openide.javascriptinterpreterengine.utils.DockerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service("daemon-impl")
public class DaemonServiceImpl implements DaemonService {
    @Autowired
    @Qualifier("docker-handler")
    private DockerHandler dockerHandler;
    @Override
    public String checkDaemon() throws IOException {
        return dockerHandler.pingDaemon();
    }
}
