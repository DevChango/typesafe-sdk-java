package io.github.premocloud.typesafe.spring;

import io.github.premocloud.typesafe.TypeSafeClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeSafeAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TypeSafeAutoConfiguration.class));

    @Test
    void createsClientWhenApiKeyIsSet() {
        runner.withPropertyValues("typesafe.api-key=apik-test", "typesafe.default-model=jev-1.12.0", "typesafe.timeout=5s")
                .run(context -> {
                    assertTrue(context.containsBean("typeSafeClient"));
                    TypeSafeProperties properties = context.getBean(TypeSafeProperties.class);
                    assertEquals("jev-1.12.0", properties.getDefaultModel());
                    assertEquals(5, properties.getTimeout().toSeconds());
                    assertEquals(TypeSafeClient.DEFAULT_BASE_URL, properties.getBaseUrl());
                });
    }

    @Test
    void environmentVariableAloneIsEnough() {
        runner.withInitializer(context -> context.getEnvironment().getPropertySources()
                        .addFirst(new SystemEnvironmentPropertySource("test-env", Map.of("TYPESAFE_API_KEY", "apik-from-env", "TYPESAFE_DEFAULT_MODEL", "jev-1.12.0"))))
                .run(context -> {
                    assertTrue(context.containsBean("typeSafeClient"));
                    assertEquals("apik-from-env", context.getBean(TypeSafeProperties.class).getApiKey());
                    assertEquals("jev-1.12.0", context.getBean(TypeSafeClient.class).defaultModel());
                });
    }

    @Test
    void staysOutOfTheWayWithoutApiKey() {
        runner.run(context -> assertFalse(context.containsBean("typeSafeClient")));
    }

    @Test
    void userDefinedClientWins() {
        runner.withPropertyValues("typesafe.api-key=apik-test")
                .withUserConfiguration(CustomClient.class)
                .run(context -> assertSame(CustomClient.CLIENT, context.getBean(TypeSafeClient.class)));
    }

    @Configuration
    static class CustomClient {
        static final TypeSafeClient CLIENT = TypeSafeClient.builder().apiKey("mine").build();

        @Bean
        TypeSafeClient typeSafeClient() {
            return CLIENT;
        }
    }
}
