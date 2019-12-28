package org.project.ecco.example;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.springframework.web.context.annotation.RequestScope;


/**
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class App {

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ThreadLocal<Locale> locale() {
        return new ThreadLocal<Locale>();
    }
}
