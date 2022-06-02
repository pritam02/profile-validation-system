package net.intuit.profilevalidation.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class HealthCheckController {
    @RequestMapping("/healthcheck")
    @ResponseBody
    public ResponseEntity reloadFM() {
        log.info("Application is up and running");
        String status = "Application is up and running";
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
