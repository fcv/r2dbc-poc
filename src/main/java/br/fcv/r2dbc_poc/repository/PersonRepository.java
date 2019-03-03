package br.fcv.r2dbc_poc.repository;

import br.fcv.r2dbc_poc.entity.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {
}
