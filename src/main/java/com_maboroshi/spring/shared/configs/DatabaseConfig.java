package com_maboroshi.spring.shared.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(System.getenv("SUPABASE_URL"));
    config.setUsername(System.getenv("SUPABASE_USER"));
    config.setPassword(System.getenv("SUPABASE_PASS"));
    config.setDriverClassName("org.postgresql.Driver");

    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setConnectionTimeout(20000);
    config.setMaxLifetime(1800000);

    return new HikariDataSource(config);
  }
}
