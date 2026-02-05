package com.example.demo;

import com.example.starter.MessageWriter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


 /*   @Bean
    MessageWriter messageWriter() {
        return () -> "HI";
    }*/

    @Bean
    ApplicationRunner runner(MessageWriter messageWriter) {
        return args -> IO.println(messageWriter.message());
    }

}
