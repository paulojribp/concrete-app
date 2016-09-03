package org.paulojr.concrete;

import org.paulojr.concrete.config.JPAConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import(value = {JPAConfiguration.class})
public class Boot {

    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }

}
