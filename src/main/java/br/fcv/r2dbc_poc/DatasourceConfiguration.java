package br.fcv.r2dbc_poc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

@Slf4j
@Configuration
@EnableR2dbcRepositories
public class DatasourceConfiguration extends AbstractR2dbcConfiguration {

    @Bean
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

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(factory)
                .validationQuery("SELECT 1")
                .maxIdleTime(Duration.ofMillis(1000))
                .initialSize(2)
                .maxSize(5)
                .build();

        log.debug("Created PostgresqlConnectionFactory out of configuration {}", config);
        return new PooledConnectionFactory(factory, poolConfiguration);
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

@Slf4j
class PooledConnectionFactory implements ConnectionFactory {

    private final ConnectionFactory factory;
    private final ConnectionPool pool;

    public PooledConnectionFactory(ConnectionFactory factory, ConnectionPoolConfiguration poolConfiguration) {
        this.factory = factory;
        this.pool = new ConnectionPool(poolConfiguration);
    }

    @Override
    public Publisher<? extends Connection> create() {
        log.trace("create()");
        return pool.create();
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        log.trace("getMetadata()");
        return factory.getMetadata();
    }

    @EventListener
    void onContextClosed(final ContextClosedEvent event) {
        log.info("About to dispose ConnectionPool {}", pool);
        pool.dispose();
    }
}
