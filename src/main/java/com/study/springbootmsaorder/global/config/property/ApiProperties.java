package com.study.springbootmsaorder.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private final String productUrl;
}
