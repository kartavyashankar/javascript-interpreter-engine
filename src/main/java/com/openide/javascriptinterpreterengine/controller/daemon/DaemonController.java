package com.openide.javascriptinterpreterengine.controller.daemon;

import com.openide.javascriptinterpreterengine.controller.MainController;
import com.openide.javascriptinterpreterengine.model.daemon.response.DaemonResponseModel;
import com.openide.javascriptinterpreterengine.service.daemon.DaemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/developer/daemon")
public class DaemonController extends MainController {
    @Autowired
    @Qualifier("daemon-impl")
    private DaemonService daemonService;

    @GetMapping("/heartbeat")
    public ResponseEntity<DaemonResponseModel> checkDaemon() throws IOException {
        String status = daemonService.checkDaemon();
        DaemonResponseModel responseModel = new DaemonResponseModel();
        responseModel.setStatus(status);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
