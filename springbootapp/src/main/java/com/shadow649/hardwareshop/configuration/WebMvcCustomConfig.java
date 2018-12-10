package com.shadow649.hardwareshop.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadow649.hardwareshop.dto.DTOModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.persistence.EntityManager;
import java.util.List;


@Configuration
public class WebMvcCustomConfig extends WebMvcConfigurationSupport {

    private static final ModelMapper modelMapper= new ModelMapper();

    private final ApplicationContext applicationContext;
    private final EntityManager entityManager;

    @Autowired
    public WebMvcCustomConfig(ApplicationContext applicationContext, EntityManager entityManager) {
        this.applicationContext = applicationContext;
        this.entityManager = entityManager;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
        argumentResolvers.add(new DTOModelMapper(objectMapper, entityManager, modelMapper()));
    }
}
