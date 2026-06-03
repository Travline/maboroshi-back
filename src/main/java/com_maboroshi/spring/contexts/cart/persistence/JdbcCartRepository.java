package com_maboroshi.spring.contexts.cart.persistence;

import com_maboroshi.spring.contexts.cart.models.CartItem;
import com_maboroshi.spring.shared.utils.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcCartRepository implements CartRepository {
  private final Slf4jLogger slf4jLogger;
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<CartItem> rowMapper = (rs, rowNum) -> CartItem.builder()
      .productId(UUID.fromString(rs.getString("product_id")))
      .productName(rs.getString("product_name"))
      .realPrice(rs.getBigDecimal("real_price").doubleValue())
      .salePrice(rs.getBigDecimal("sale_price").doubleValue())
      .slug(rs.getString("slug"))
      .quantity(rs.getInt("quantity"))
      .build();

  @Override
  public Optional<List<CartItem>> findAllCartItemsByUserId(UUID userId) {
    String sql = """
        SELECT 
            c.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            c.quantity
        FROM carts c
        INNER JOIN products p ON c.product_id = p.id
        WHERE c.user_id = ?
        """;

    try {
      List<CartItem> items = jdbcTemplate.query(sql, rowMapper, userId);

      if (items.isEmpty()) {
        return Optional.empty();
      }

      return Optional.of(items);

    } catch (Exception e) {
      slf4jLogger.error("Find items cart", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<CartItem> saveCartItemByUserId(UUID userId, UUID productId) {
    String insertSql = """
        INSERT INTO carts (user_id, product_id, quantity) 
        VALUES (?, ?, 1);
        """;

    String selectSql = """
        SELECT 
            c.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            c.quantity
        FROM carts c
        INNER JOIN products p ON c.product_id = p.id
        WHERE c.user_id = ? AND c.product_id = ?
        """;

    try {
      jdbcTemplate.update(insertSql, userId, productId);
      List<CartItem> results = jdbcTemplate.query(selectSql, rowMapper, userId, productId);
      return results.stream().findFirst();
    } catch (Exception e) {
      slf4jLogger.error("Saving item in cart", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<CartItem> deleteCartItemByUserId(UUID userId, UUID productId) {
    String selectSql = """
        SELECT 
            c.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            c.quantity
        FROM carts c
        INNER JOIN products p ON c.product_id = p.id
        WHERE c.user_id = ? AND c.product_id = ?
        """;

    String deleteSql = """
        DELETE FROM carts WHERE user_id = ? AND product_id = ?
        """;

    try {
      List<CartItem> items = jdbcTemplate.query(selectSql, rowMapper, userId, productId);

      if (!items.isEmpty()) {
        jdbcTemplate.update(deleteSql, userId, productId);
        return Optional.of(items.get(0));
      }

      return Optional.empty();
    } catch (Exception e) {
      slf4jLogger.error("Deleting item from cart", e);
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public Optional<CartItem> updateCartItemQuantityByUserId(UUID userId, UUID productId, int quantity) {

    if (quantity <= 0) {
      slf4jLogger.warn("Intento de actualizar cantidad a un valor no válido: " + quantity);
      return Optional.empty();
    }

    String checkStockSql = "SELECT stock FROM products WHERE id = ?";

    String updateSql = """
        UPDATE carts 
        SET quantity = ? 
        WHERE user_id = ? AND product_id = ?
        """;

    String selectSql = """
        SELECT 
            c.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            c.quantity
        FROM carts c
        INNER JOIN products p ON c.product_id = p.id
        WHERE c.user_id = ? AND c.product_id = ?
        """;

    try {
      Integer currentStock = jdbcTemplate.queryForObject(checkStockSql, Integer.class, productId);

      if (currentStock == null || quantity > currentStock) {
        slf4jLogger.warn("No hay suficiente stock. Requerido: " + quantity + ", Disponible: " + currentStock);
        return Optional.empty();
      }

      int rowsAffected = jdbcTemplate.update(updateSql, quantity, userId, productId);

      if (rowsAffected == 0) {
        slf4jLogger.warn("No se encontró el registro en el carrito para el usuario " + userId + " y producto " + productId);
        return Optional.empty();
      }

      List<CartItem> results = jdbcTemplate.query(selectSql, rowMapper, userId, productId);
      return results.stream().findFirst();

    } catch (Exception e) {
      slf4jLogger.error("Error al actualizar el item en el carrito: " + e.getMessage(), e);
      return Optional.empty();
    }
  }
}