package com_maboroshi.spring.contexts.wishlist.persistence;

import com_maboroshi.spring.contexts.wishlist.models.WishlistItem;
import com_maboroshi.spring.shared.utils.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcWishlistRepository implements WishlistRepository {
  private final Slf4jLogger slf4jLogger;
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<WishlistItem> rowMapper = (rs, rowNum) -> WishlistItem.builder()
      .productId(UUID.fromString(rs.getString("product_id")))
      .productName(rs.getString("product_name"))
      .realPrice(rs.getBigDecimal("real_price").doubleValue())
      .salePrice(rs.getBigDecimal("sale_price").doubleValue())
      .slug(rs.getString("slug"))
      .build();

  @Override
  public Optional<List<WishlistItem>> findAllWishlistItemsByUserId(UUID userId) {
    String sql = """
        SELECT 
            w.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug
        FROM wishlists w
        INNER JOIN products p ON w.product_id = p.id
        WHERE w.user_id = ?
        """;

    try {
      List<WishlistItem> items = jdbcTemplate.query(sql, rowMapper, userId);

      if (items.isEmpty()) {
        return Optional.empty();
      }

      return Optional.of(items);

    } catch (Exception e) {
      slf4jLogger.error("Find items wishlist", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<WishlistItem> saveWishlistItemByUserId(UUID userId, UUID productId) {
    String insertSql = """
        INSERT INTO wishlists (user_id, product_id) 
        VALUES (?, ?)
        ON CONFLICT (user_id, product_id) DO NOTHING; 
        """;

    String selectSql = """
        SELECT 
            w.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug
        FROM wishlists w
        INNER JOIN products p ON w.product_id = p.id
        WHERE w.user_id = ? AND w.product_id = ?
        """;

    try {
      jdbcTemplate.update(insertSql, userId, productId);
      List<WishlistItem> results = jdbcTemplate.query(selectSql, rowMapper, userId, productId);
      return results.stream().findFirst();
    } catch (Exception e) {
      slf4jLogger.error("Saving item in wishlist", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<WishlistItem> deleteWishlistItemByUserId(UUID userId, UUID productId) {
    String selectSql = """
        SELECT 
            w.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug
        FROM wishlists w
        INNER JOIN products p ON w.product_id = p.id
        WHERE w.user_id = ? AND w.product_id = ?
        """;

    String deleteSql = """
        DELETE FROM wishlists WHERE user_id = ? AND product_id = ?
        """;

    try {
      List<WishlistItem> items = jdbcTemplate.query(selectSql, rowMapper, userId, productId);

      if (!items.isEmpty()) {
        jdbcTemplate.update(deleteSql, userId, productId);
        return Optional.of(items.get(0));
      }

      return Optional.empty();
    } catch (Exception e) {
      slf4jLogger.error("Deleting item from wishlist", e);
      return Optional.empty();
    }
  }
}