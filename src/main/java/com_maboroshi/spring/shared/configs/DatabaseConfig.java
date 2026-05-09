package com_maboroshi.spring.shared.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    config.setJdbcUrl(dotenv.get("SUPABASE_URL"));
    config.setUsername(dotenv.get("SUPABASE_USER"));
    config.setPassword(dotenv.get("SUPABASE_PASS"));
    config.setDriverClassName("org.postgresql.Driver");

    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setConnectionTimeout(20000);
    config.setMaxLifetime(1800000);

    return new HikariDataSource(config);
  }
}
