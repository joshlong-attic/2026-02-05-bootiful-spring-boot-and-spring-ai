package com.example.adoptions;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.lang.annotation.*;

//@Import(MyBeanRegistrar.class)
@SpringBootApplication
public class AdoptionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdoptionsApplication.class, args);
    }

}

/*

@Configuration
class MyJavaConfig {


    @Bean
    MyClass myClass() {
        return new MyClass();
    }

}
*/

// 0. ingest (application-context.xml, Java configuration, component scanning, etc.)
// 1. BeanDefinitions
// 2. beans!

class MyBeanRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {
        for (var i = 0; i < 10; i++) {
            var counter = i;
            registry.registerBean(MyClass.class, s -> s
                    .supplier(supplierContext -> {
                        var dataSource = supplierContext.bean(DataSource.class);
                        return new MyClass(dataSource, counter);
                    }));
        }
    }
}

//@TurkishCoffeeBean
class MyClass {

    MyClass(DataSource dataSource, int counter) {
        Assert.notNull(dataSource, "the db should not be null!");
        IO.println("got the DB! hi #" + counter + "!");
    }
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@RestController
@interface TurkishCoffeeBean {

    /**
     * Alias for {@link Component#value}.
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

}
