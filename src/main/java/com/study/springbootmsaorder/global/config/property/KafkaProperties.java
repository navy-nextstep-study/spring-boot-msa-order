package com.study.springbootmsaorder.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private final String url;
    private final String groupId;
}
