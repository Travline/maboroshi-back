package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductsCannotBeRetrieved;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ArtistQueries {

  private final JdbcTemplate jdbcTemplate;
  private final ProductRowMapper productRowMapper;
  private final AppLogger appLogger;

  public ArtistQueries(JdbcTemplate jdbcTemplate, ProductRowMapper productRowMapper, AppLogger appLogger) {
    this.jdbcTemplate = jdbcTemplate;
    this.productRowMapper = productRowMapper;
    this.appLogger = appLogger;
  }

  public Result<Artist[], RepositoryError> getAllArtists() {
    String sql = "SELECT id, name, image FROM artists ORDER BY name ASC";
    try {
      List<Artist> results = jdbcTemplate.query(sql, (rs, rowNum) -> new Artist(
          UUID.fromString(rs.getString("id")),
          rs.getString("name"),
          rs.getString("image")
      ));
      return Result.ok(results.toArray(new Artist[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching artists", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }

  public Result<Artist, RepositoryError> getArtistByName(String name) {
    String sql = "SELECT id, name, image FROM artists WHERE name = ?";
    try {
      Artist artist = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Artist(
          UUID.fromString(rs.getString("id")),
          rs.getString("name"),
          rs.getString("image")
      ), name);

      if (artist == null) {
        return Result.fail(new ProductNotFound("Artist not found: " + name));
      }
      return Result.ok(artist);
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching artist by name", e);
      return Result.fail(new ProductNotFound(e.getMessage()));
    }
  }

  public Result<BaseProduct[], RepositoryError> getProductsByArtistName(String name) {
    String sql = """
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug, p.type, p.status,
               a.image AS artist_image,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
        FROM products p
        LEFT JOIN artists a ON a.id = p.artist_id
        WHERE p.is_visible = true
          AND p.artist ILIKE ?
        ORDER BY p.id DESC
        """;
    try {
      List<BaseProduct> results = jdbcTemplate.query(sql, productRowMapper, name);
      return Result.ok(results.toArray(new BaseProduct[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching products by artist", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }
}
