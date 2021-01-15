package org.mike.licenses.controllers;

import org.mike.licenses.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassCheckController {

    @Autowired
    private ServiceConfig config;

    @GetMapping("/pass")
    public String pass() {
        return config.getDbPassword();
    }
}
