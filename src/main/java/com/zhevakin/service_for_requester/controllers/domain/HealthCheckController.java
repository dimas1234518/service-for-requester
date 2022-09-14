package com.zhevakin.service_for_requester.controllers.domain;

import com.zhevakin.service_for_requester.models.Variable;
import org.springframework.http.MediaType;
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

    @RequestMapping(value = "/checkbody", method = RequestMethod.GET)
    public ResponseEntity<Variable> checkJson() {
        return ResponseEntity.ok(new Variable("test", "test", "test"));
    }

}
