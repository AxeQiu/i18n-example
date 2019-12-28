package org.project.ecco.example.aspect;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 */
@Aspect
@Component
@lombok.extern.slf4j.Slf4j
public class AppControllerAspect {

    @Autowired
    private ThreadLocal<Locale> threadLocalLocale;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Around("execution(* org.project.ecco.example.controller.*.*(..))")
    public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
        Locale locale = httpServletRequest.getLocale();
        threadLocalLocale.set(locale);
        Object obj = null;
        try {
            log.debug("in the controller aspect, the locale is: " + threadLocalLocale.get());
            obj = pjp.proceed();
        } catch (Throwable t) {
            obj = new ResponseEntity<String>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            //prevent from rehash /resize of ThreadLocalMap
            threadLocalLocale.remove();
        }
        return obj;
    }
}
