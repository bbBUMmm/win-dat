package org.windat.main;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.windat.ws.mapper.DateMapper;

@Component
public class DateBeanConfiguration {

    @Bean
    public DateMapper dateMapper() {
        return new DateMapper() {
        };
    }
}
