package com.example.adoptions.adoptions;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Controller
@ResponseBody
class DogsController {

    private final DogRepository repository;

    DogsController(DogRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/dogs" ,version = "1.1")
    Collection<Dog> dogs(@RequestParam(required = false) Optional<String> name) {
        return name.map(this.repository::findByName).orElse(this.repository.findAll());
    }

    @GetMapping(value = "/dogs" ,version = "1.0")
    Collection<Map<String, Object>> list() {
        return repository
                .findAll()
                .stream()
                .map(dog -> Map.of("id", (Object) dog.id(), "fullName", dog.name(),
                        "description", dog.description()))
                .toList();

    }
}
