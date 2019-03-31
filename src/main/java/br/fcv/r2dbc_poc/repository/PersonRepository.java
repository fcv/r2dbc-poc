package br.fcv.r2dbc_poc.repository;

import br.fcv.r2dbc_poc.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query(value = "SELECT p.* from person as p, (select pg_sleep(?)) as delay", nativeQuery = true)
    List<Person> findAllWithDelay(final int delayInSeconds);
}
