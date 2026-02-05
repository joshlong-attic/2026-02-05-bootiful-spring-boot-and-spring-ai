package com.example.adoptions.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Transactional
@Controller
@ResponseBody
class AdoptionsController {


    private final DogRepository repository;
    private final ApplicationEventPublisher applicationEventPublisher;

    AdoptionsController(DogRepository repository, ApplicationEventPublisher applicationEventPublisher) {
        this.repository = repository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("/dogs/{dogId}/adoptions")
    void adopt(@PathVariable int dogId, @RequestParam String owner) {
        this.repository.findById(dogId).ifPresent(dog -> {
            var updated = this.repository.save(new Dog(
                    dog.id(), owner, dog.name(), dog.description()
            ));
            IO.println("adopted " + updated);

            this.applicationEventPublisher.publishEvent(new DogAdoptedEvent(dogId));
        });

    }
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {

    Collection <Dog> findByName (String name) ;
}

// look mom, no Lombok!
record Dog(@Id int id, String owner, String name, String description) {
}