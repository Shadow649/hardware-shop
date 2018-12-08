package com.shadow649.hardwareshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadow649.hardwareshop.dto.DTOModelMapper;
import org.h2.server.web.WebServlet;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.persistence.EntityManager;
import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootApplication
@Configuration
public class TestApplication extends WebMvcConfigurationSupport {

    private static final ModelMapper modelMapper = new ModelMapper();

    private final ApplicationContext applicationContext;
    private final EntityManager entityManager;

    @Autowired
    public TestApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        entityManager = mock(EntityManager.class);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }

    @Bean
    public EntityManager entityManager() {
        return entityManager;
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
        argumentResolvers.add(new DTOModelMapper(objectMapper, entityManager, modelMapper()));
    }

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<>( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
