package org.project.ecco.example.controller;

import org.project.ecco.example.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AppController {
    
    @Autowired
    private AppService service;

    @GetMapping("/say-hello")
    public ResponseEntity<?> sayHello() {
        return new ResponseEntity<Object>(service.sayHello(), HttpStatus.OK);
    }
}
