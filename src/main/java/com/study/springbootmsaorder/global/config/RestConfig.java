package com.study.springbootmsaorder.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.study.springbootmsaorder.global.api.ProductHttpClient;
import com.study.springbootmsaorder.global.config.property.ApiProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestConfig {

    private final ApiProperties apiProperties;

    @Bean
    public ProductHttpClient productHttpClient() {
        final RestClient restClient = RestClient.builder()
                .baseUrl(apiProperties.getProductUrl())
                .build();

        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ProductHttpClient.class);
    }
}
