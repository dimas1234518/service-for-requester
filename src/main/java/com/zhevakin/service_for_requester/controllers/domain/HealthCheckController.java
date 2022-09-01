package com.zhevakin.service_for_requester.controllers.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HealthCheckController {

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("OK");
    }

}
