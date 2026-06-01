package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductsCannotBeRetrieved;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductSearchQueries {

  private final JdbcTemplate jdbcTemplate;
  private final ProductRowMapper rowMapper;
  private final AppLogger appLogger;

  public ProductSearchQueries(JdbcTemplate jdbcTemplate, ProductRowMapper rowMapper, AppLogger appLogger) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
    this.appLogger = appLogger;
  }

  public Result<BaseProduct[], RepositoryError> searchProducts(String term) {
    String sql = """
        SELECT DISTINCT ON (p.id) p.id, p.product_name, p.artist, p.real_price, p.sale_price,
       p.stock, p.slug, p.type, p.status,
       a.image AS artist_image,
       ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
FROM products p
LEFT JOIN artists a ON a.id = p.artist_id
LEFT JOIN tracklists t ON t.product_id = p.id
WHERE p.is_visible = true
  AND (
    p.product_name ILIKE ?
    OR p.artist ILIKE ?
    OR t.song ILIKE ?
  )
ORDER BY p.id DESC
        """;
    String likeTerm = "%" + term + "%";
    try {
      List<BaseProduct> results = jdbcTemplate.query(sql, rowMapper, likeTerm, likeTerm, likeTerm);
      return Result.ok(results.toArray(new BaseProduct[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error searching products", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }

  public Result<BaseProduct[], RepositoryError> getRecommendedProducts(String[] names) {
    if (names == null || names.length == 0) {
      return Result.fail(new ProductNotFound("Names list is empty"));
    }

    String placeholders = String.join(", ", Collections.nCopies(names.length, "?"));
    String sql = String.format("""
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug, p.type, p.status,
               a.image AS artist_image,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
        FROM products p
        LEFT JOIN artists a ON a.id = p.artist_id
        WHERE p.is_visible = true
          AND p.product_name = ANY(ARRAY[%s]::TEXT[])
        """, placeholders);

    try {
      List<BaseProduct> results = jdbcTemplate.query(sql, rowMapper, (Object[]) names);
      return Result.ok(results.toArray(new BaseProduct[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching recommended products", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }
}
