package br.fcv.r2dbc_poc.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class Greeting {
    private final String id;
    private final String source;
    private final Instant at;
}
