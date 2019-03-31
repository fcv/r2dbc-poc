package br.fcv.r2dbc_poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Wither
@Builder
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDate birthday;

}
