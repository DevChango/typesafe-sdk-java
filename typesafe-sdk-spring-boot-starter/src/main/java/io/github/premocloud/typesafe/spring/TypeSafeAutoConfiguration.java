package io.github.premocloud.typesafe.spring;

import io.github.premocloud.typesafe.TypeSafeClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Exposes a {@link TypeSafeClient} bean when {@code typesafe.api-key} is set. Your own bean of that type wins. */
@AutoConfiguration
@EnableConfigurationProperties(TypeSafeProperties.class)
@ConditionalOnProperty(prefix = "typesafe", name = "api-key")
public class TypeSafeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TypeSafeClient typeSafeClient(TypeSafeProperties typeSafeProperties, ObjectProvider<ObjectMapper> objectMapper) {
        TypeSafeClient.Builder builder = TypeSafeClient.builder()
                .apiKey(typeSafeProperties.getApiKey())
                .baseUrl(typeSafeProperties.getBaseUrl())
                .defaultModel(typeSafeProperties.getDefaultModel())
                .timeout(typeSafeProperties.getTimeout());
        objectMapper.ifAvailable(builder::objectMapper);
        return builder.build();
    }
}
