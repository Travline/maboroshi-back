package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.DetailedProduct;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductDetailQueries {

  private final JdbcTemplate jdbcTemplate;
  private final AppLogger appLogger;

  public ProductDetailQueries(JdbcTemplate jdbcTemplate, AppLogger appLogger) {
    this.jdbcTemplate = jdbcTemplate;
    this.appLogger = appLogger;
  }

  public Result<DetailedProduct, RepositoryError> getProductDetail(String slug) {
    String sql = """
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images,
               ARRAY(SELECT t.song FROM tracklists t WHERE t.product_id = p.id ORDER BY t.id) AS tracklist,
               ARRAY(SELECT g.genre_name FROM genres g
                     JOIN product_genres pg ON pg.genre_id = g.id
                     WHERE pg.product_id = p.id) AS genres
        FROM products p
        WHERE p.slug = ?
          AND p.is_visible = true
        """;
    try {
      DetailedProduct product = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
        String[] images    = (String[]) rs.getArray("images").getArray();
        String[] tracklist = (String[]) rs.getArray("tracklist").getArray();
        String[] genres    = (String[]) rs.getArray("genres").getArray();
        return new DetailedProduct(
            UUID.fromString(rs.getString("id")),
            rs.getString("product_name"),
            rs.getString("artist"),
            rs.getDouble("real_price"),
            rs.getDouble("sale_price"),
            rs.getInt("stock"),
            rs.getString("slug"),
            images,
            false,
            false,
            tracklist,
            genres
        );
      }, slug);

      if (product == null) {
        return Result.fail(new ProductNotFound("Product not found: " + slug));
      }
      return Result.ok(product);
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching product detail", e);
      return Result.fail(new ProductNotFound(e.getMessage()));
    }
  }
}