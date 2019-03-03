package br.fcv.r2dbc_poc;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Slf4j
@Configuration
@EnableR2dbcRepositories
public class DatasourceConfiguration extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {

        final PostgresqlConnectionConfigurationBean config = connectionConfiguration();

        final PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
                .applicationName(config.getApplicationName())
                .host(config.getHost())
                .port(config.getPort())
                .username(config.getUsername())
                .password(config.getPassword())
                .database(config.getDatabase())
                .build();

        final PostgresqlConnectionFactory factory = new PostgresqlConnectionFactory(configuration);
        log.debug("Created PostgresqlConnectionFactory out of configuration {}", config);
        return factory;
    }

    @Bean
    @ConfigurationProperties(prefix = "br.fcv.r2dbc-poc.data-source")
    public PostgresqlConnectionConfigurationBean connectionConfiguration() {
        return new PostgresqlConnectionConfigurationBean();
    }

    @Data
    public static class PostgresqlConnectionConfigurationBean {
        private static final int DEFAULT_PORT = 5432;
        private String applicationName;
        private String host;
        private int port = DEFAULT_PORT;
        private String username;
        private String password;
        private String database;

    }
}
