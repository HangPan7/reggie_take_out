package com.hanghang.reggie.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghang.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class webMvcConfig extends WebMvcConfigurationSupport {

    /**
     * mvc框架转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置转换器对象底层使用Jackson将java对象转化为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //将转化器对象追加到转化器集合中，且设置到0号位优先调用
        converters.add(0 ,mappingJackson2HttpMessageConverter) ;
    }

    /**
     * 将静态资源放行
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/") ;
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/") ;
    }
}
