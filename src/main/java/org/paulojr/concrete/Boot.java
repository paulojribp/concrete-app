package org.paulojr.concrete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Boot {

    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }

}
