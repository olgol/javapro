package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.example.demo")
public class AppConfig {

    /* ------------- Connection Pool ----------------- */
    @Bean
    public DataSource dataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        cfg.setUsername("postgres");
        cfg.setPassword("password");
        cfg.setMaximumPoolSize(10);
        cfg.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(cfg);
    }

    /* ------------- JdbcTemplate ------------------- */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}