package br.fcv.r2dbc_poc.repository;

import br.fcv.r2dbc_poc.entity.Person;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {

    @Query("SELECT p.* from person as p, (select pg_sleep($1)) as delay")
    Flux<Person> findAllWithDelay(final int delayInSeconds);
}
