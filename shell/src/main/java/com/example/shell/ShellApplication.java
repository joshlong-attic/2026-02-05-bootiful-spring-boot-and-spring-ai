package com.example.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ShellApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShellApplication.class, args);
    }


}

@Component
class MyShellCli {

    @Command
    String hello() {
        return "Hello World!";
    }
}
