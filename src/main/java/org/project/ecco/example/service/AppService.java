package org.project.ecco.example.service;

import org.project.ecco.example.service.rt.*;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class AppService {

    public Object sayHello() {
        int  k = ThreadLocalRandom.current().nextInt(10);
        if (k % 2 != 0) {
            throw new MyServiceException1();
        } else if (k % 3 != 0) {
            throw new MyServiceException2();
        } else {
            return "HELLO";
        }
    }
}
