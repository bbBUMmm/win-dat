package org.windat.main;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"org.windat.*"})
//@EnableJpaRepositories("org.windat.jpa.repository")
// For monthly prizes
@EnableScheduling
public class WinDatApplication
{
    public static void main(String[] args)
    {
        Dotenv dotenv = Dotenv.configure()
//                Root of the project
                .directory(System.getProperty("user.dir"))
//                Name of the env file
                .filename(".env.dev")
                .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(WinDatApplication.class, args);
    }
}
