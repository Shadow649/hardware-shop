package com.shadow649.hardwareshop.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import java.util.Collection;

/**
 * Customize the response return body value to DTO before JSON serialization
 *
 * @author Emanuele Lombardi
 */
@ControllerAdvice
public class DtoMapperResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType) && returnType.hasMethodAnnotation(DTO.class);
    }
    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
        DTO ann = returnType.getMethodAnnotation(DTO.class);
        Assert.state(ann != null, "No Dto annotation");
        Class<?> dtoType = ann.value();
        Object value = bodyContainer.getValue();
        Object returnValue;
        if (value instanceof Collection) {
            returnValue = ((Collection<?>) value).stream().map(it -> modelMapper.map(it, dtoType));
        } else {
            returnValue = modelMapper.map(value, dtoType);
        }
        bodyContainer.setValue(returnValue);
    }
}
