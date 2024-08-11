package com.study.springbootmsaorder.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.study.springbootmsaorder.global.config.property.ApiProperties;
import com.study.springbootmsaorder.global.config.property.KafkaProperties;

@Configuration
@EnableConfigurationProperties(value = {
        ApiProperties.class,
        KafkaProperties.class
})
public class PropertyConfig {

}
