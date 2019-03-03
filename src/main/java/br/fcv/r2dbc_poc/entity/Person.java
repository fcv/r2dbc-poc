package br.fcv.r2dbc_poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Wither
@Builder
public class Person {

    @Id
    private Long id;
    private String name;
    private LocalDate birthday;

}
