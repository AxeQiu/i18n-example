package org.project.ecco.example.aspect;

import org.project.ecco.example.service.rt.*;

import java.util.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Aspect
@Component
@lombok.extern.slf4j.Slf4j
public class AppServiceAspect {

    @Autowired
    private ThreadLocal<Locale> threadLocalLocale;

    @Autowired
    private MessageSource i18n;

    @Around("execution(* org.project.ecco.example.service.*.*(..))")
    public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("in the service aspect, the locale is: " + threadLocalLocale.get());
        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (Throwable t) {
            if (t instanceof I18nMessage) {
                String messageKey = ((I18nMessage)t).getMessageKey();
                String i18nMessage = i18n.getMessage(messageKey, null, threadLocalLocale.get());
                throw new MyServiceException(i18nMessage);
            }
        }
        return obj;
    }
}
