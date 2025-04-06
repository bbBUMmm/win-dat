package org.windat.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.windat.*"})
//@EnableJpaRepositories("org.windat.jpa.repository")
public class WinDatApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WinDatApplication.class, args);
    }
}
