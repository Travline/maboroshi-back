package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductsCannotBeRetrieved;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtistQueries {

  private final JdbcTemplate jdbcTemplate;
  private final ArtistRowMapper rowMapper;
  private final AppLogger appLogger;

  public ArtistQueries(
      JdbcTemplate jdbcTemplate,
      ArtistRowMapper rowMapper,
      AppLogger appLogger
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
    this.appLogger = appLogger;
  }

  public Result<Artist[], RepositoryError> getArtists() {

    String sql = """
        SELECT
            id,
            name,
            image
        FROM artists
        ORDER BY name
        """;

    try {
      List<Artist> results = jdbcTemplate.query(sql, rowMapper);
      return Result.ok(results.toArray(new Artist[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching artists", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }
}