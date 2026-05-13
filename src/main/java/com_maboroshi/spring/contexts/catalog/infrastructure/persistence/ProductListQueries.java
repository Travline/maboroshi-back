package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductsCannotBeRetrieved;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductListQueries {

  private final JdbcTemplate jdbcTemplate;
  private final ProductRowMapper rowMapper;
  private final AppLogger appLogger;

  public ProductListQueries(JdbcTemplate jdbcTemplate, ProductRowMapper rowMapper, AppLogger appLogger) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = rowMapper;
    this.appLogger = appLogger;
  }

  public Result<BaseProduct[], RepositoryError> getLastProducts(int length) {
    String sql = """
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
        FROM products p
        WHERE p.is_visible = true
        ORDER BY p.id DESC
        LIMIT ?
        """;
    return executeListQuery(sql, length);
  }

  public Result<BaseProduct[], RepositoryError> getMostViewedProducts(int length) {
    String sql = """
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
        FROM products p
        WHERE p.is_visible = true
        ORDER BY p.views DESC
        LIMIT ?
        """;
    return executeListQuery(sql, length);
  }

  public Result<BaseProduct[], RepositoryError> getDiscountedProducts(int length) {
    String sql = """
        SELECT p.id, p.product_name, p.artist, p.real_price, p.sale_price,
               p.stock, p.slug,
               ARRAY(SELECT url FROM product_images WHERE product_id = p.id) AS images
        FROM products p
        WHERE p.is_visible = true
          AND p.sale_price IS NOT NULL
          AND p.sale_price < p.real_price
        ORDER BY (p.real_price - p.sale_price) DESC
        LIMIT ?
        """;
    return executeListQuery(sql, length);
  }

  private Result<BaseProduct[], RepositoryError> executeListQuery(String sql, int length) {
    try {
      List<BaseProduct> results = jdbcTemplate.query(sql, rowMapper, length);
      return Result.ok(results.toArray(new BaseProduct[0]));
    } catch (DataAccessException e) {
      appLogger.error("Database error fetching products", e);
      return Result.fail(new ProductsCannotBeRetrieved(e.getMessage()));
    }
  }
}