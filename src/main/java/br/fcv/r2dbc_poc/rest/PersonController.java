package br.fcv.r2dbc_poc.rest;

import br.fcv.r2dbc_poc.entity.Person;
import br.fcv.r2dbc_poc.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@Slf4j
@AllArgsConstructor

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository repository;

    @GetMapping
    public Flux<Person> listAll() {
        log.debug("listAll()");
        Flux<Person> persons = repository.findAll()
                .map(p -> {
                    // silly transformation .. just to experimenting purposes
                    log.debug("Modifying Person instance #{}", p.getId());
                    Person modifiedPerson = p.withName(p.getName() + "#" + p.getId());
                    return modifiedPerson;
                })
                .log(log.getName() +  ".listAll.flux", Level.FINE);
        log.debug("About to return persons Flux");
        return persons;
    }

    @PostMapping
    public Mono<Person> create(@RequestBody final Person person) {
        log.debug("About to persist {}", person);
        Mono<Person> persistedPerson = repository.save(person)
                .log(log.getName() + ".create.mono", Level.FINE);
        log.debug("About to return persisted person");
        return persistedPerson;
    }

}
