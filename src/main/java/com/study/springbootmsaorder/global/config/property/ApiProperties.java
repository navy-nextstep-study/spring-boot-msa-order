package com.study.springbootmsaorder.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties(value = "api")
public class ApiProperties {

    private String productUrl;
}
