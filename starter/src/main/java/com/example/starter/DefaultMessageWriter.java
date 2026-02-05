package com.example.starter;

import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.nio.charset.Charset;

class DefaultMessageWriter implements MessageWriter {

    private final String subject;

    DefaultMessageWriter() {
        this.subject = "World";
    }

    DefaultMessageWriter(String subject) {
        this.subject = subject;
    }


    @Override
    public String message() {
        return "hello, " + subject + "!";
    }
}

@ImportRuntimeHints(MessageWriterAutoConfiguration.MessageWriterRuntimeHintsRegistrar.class)
@EnableConfigurationProperties(MessageWriterConfigurationProperties.class)
@Configuration
class MessageWriterAutoConfiguration {

    static class MessageWriterRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
            hints.resources().registerResource(MESSAGE_FILE);
        }
    }

    @Bean
    static MessageWriterBeanFactoryInitializationAotProcessor messageWriterBeanFactoryInitializationAotProcessor() {
        return new MessageWriterBeanFactoryInitializationAotProcessor();
    }

    static class MessageWriterBeanFactoryInitializationAotProcessor
            implements BeanFactoryInitializationAotProcessor {

        @Override
        public @Nullable BeanFactoryInitializationAotContribution processAheadOfTime(
                ConfigurableListableBeanFactory beanFactory) {

            return (generationContext, beanFactoryInitializationCode) -> {

                for (var beanName : beanFactory.getBeanDefinitionNames()) {
                    var definition = beanFactory.getBeanDefinition(beanName);
                    var clzzz = beanFactory.getType(beanName);
                    IO.println(beanName + ":" + clzzz);
                    if (Serializable.class.isAssignableFrom(clzzz))
                        generationContext.getRuntimeHints()
                                .serialization().registerType(TypeReference.of(clzzz));
                }
            };
        }
    }

    static final Resource MESSAGE_FILE = new ClassPathResource("/messsage");

    @Bean
    ApplicationRunner fileWriter() {
        return a -> IO.println(MESSAGE_FILE
                .getContentAsString(Charset.defaultCharset()));
    }

    @Bean
    ApplicationRunner envRunner(
            Environment environment,
            @Value("${server.port}") int port,
            @Value("${a.b.c}") String abc) {
        return a -> IO.println(port + ":" + abc + ":" + environment.getProperty("a.b.c"));
    }

    @Bean
    @ConditionalOnMissingBean(MessageWriter.class)
    DefaultMessageWriter defaultMessageWriter(MessageWriterConfigurationProperties configuration) {
        if (StringUtils.hasText(configuration.name()))
            return new DefaultMessageWriter(configuration.name());
        else return new DefaultMessageWriter();
    }
}

// PGO (profile guided optimization)
// thomas wuerthinger + josh long @ devoxx belgium 2025
// bootiful graalvm


@ConfigurationProperties(prefix = "springonetour")
record MessageWriterConfigurationProperties(String name, int magicNumber, String abc) {
}