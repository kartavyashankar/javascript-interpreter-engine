package com.openide.javascriptinterpreterengine.service.daemon.impl;

import com.openide.javascriptinterpreterengine.service.daemon.DaemonService;
import com.openide.javascriptinterpreterengine.utils.DockerHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service("daemon-impl")
public class DaemonServiceImpl implements DaemonService {
    @Override
    public String checkDaemon() throws IOException {
        return DockerHandler.pingDaemon();
    }
}
