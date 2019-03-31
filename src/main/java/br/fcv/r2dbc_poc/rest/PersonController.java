package br.fcv.r2dbc_poc.rest;

import br.fcv.r2dbc_poc.entity.Person;
import br.fcv.r2dbc_poc.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@AllArgsConstructor

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository repository;

    @GetMapping
    public Stream<Person> listAll(@RequestParam("delay") final Optional<Duration> delay) {
        log.debug("listAll(delay: {})", delay);

        Stream<Person> persons = delay
                .filter(d -> !d.isNegative())
                .map(d -> {
                    final int ds =  (int) d.getSeconds();
                    log.debug("Querying `findAll` with a delay of {} seconds", ds);
                    return repository.findAllWithDelay(ds).stream();
                })
                .orElseGet(() -> StreamSupport.stream(repository.findAll().spliterator(), false))
                .map(p -> {
                    // silly transformation .. just to experimenting purposes
                    log.debug("Modifying Person instance #{}", p.getId());
                    Person modifiedPerson = p.withName(p.getName() + "#" + p.getId());
                    return modifiedPerson;
                });
        log.debug("About to return persons Flux");
        return persons;
    }

    @PostMapping
    public Person create(@RequestBody final Person person) {
        log.debug("About to persist {}", person);
        Person persistedPerson = repository.save(person);
        log.debug("About to return persisted person");
        return persistedPerson;
    }

}
