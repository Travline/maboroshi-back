package com_maboroshi.spring.contexts.order.persistence;

import com_maboroshi.spring.contexts.order.errors.CannotCreateOrder;
import com_maboroshi.spring.contexts.order.models.OrderItem;
import com_maboroshi.spring.shared.utils.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {
  private final Slf4jLogger slf4jLogger;
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> OrderItem.builder()
      .productId(UUID.fromString(rs.getString("product_id")))
      .productName(rs.getString("product_name"))
      .realPrice(rs.getBigDecimal("real_price").doubleValue())
      .salePrice(rs.getBigDecimal("sale_price") != null ? rs.getBigDecimal("sale_price").doubleValue() : null)
      .slug(rs.getString("slug"))
      .quantity(rs.getInt("quantity"))
      .date(rs.getObject("created_at", OffsetDateTime.class))
      .build();

  @Override
  @Transactional
  public Optional<List<OrderItem>> createOrder(UUID userId) {
    String selectCartWithStockSql = """
        SELECT 
            c.product_id, 
            c.quantity, 
            p.stock,
            p.product_name
        FROM carts c
        INNER JOIN products p ON c.product_id = p.id
        WHERE c.user_id = ?
        """;

    String insertSaleSql = """
        INSERT INTO sales (sale_group, user_id, product_id, quantity) 
        VALUES (?, ?, ?, ?)
        """;

    String updateStockSql = """
        UPDATE products 
        SET stock = stock - ? 
        WHERE id = ?
        """;

    String deleteCartSql = """
        DELETE FROM carts WHERE user_id = ?
        """;

    String selectOrderSql = """
        SELECT 
            s.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            s.quantity,
            s.created_at
        FROM sales s
        INNER JOIN products p ON s.product_id = p.id
        WHERE s.sale_group = ?
        """;

    try {
      // 1. Get current cart items with stock info
      List<CartItemWithStockDto> cartItems = jdbcTemplate.query(
          selectCartWithStockSql, 
          (rs, rowNum) -> new CartItemWithStockDto(
              UUID.fromString(rs.getString("product_id")),
              rs.getInt("quantity"),
              rs.getInt("stock"),
              rs.getString("product_name")
          ), 
          userId
      );

      if (cartItems.isEmpty()) {
        slf4jLogger.warn("Attempted to create order for empty cart: " + userId);
        return Optional.empty();
      }

      // 2. Validate stock for each item in the cart
      for (CartItemWithStockDto item : cartItems) {
        if (item.quantity > item.stock) {
          throw new CannotCreateOrder(
              "Not enough stock for product: " + item.productName + 
              ". Requested: " + item.quantity + ", Available: " + item.stock
          );
        }
      }

      // 3. Generate a unique sale group
      UUID saleGroup = UUID.randomUUID();

      // 4. Record the sale and adjust the product stock
      for (CartItemWithStockDto item : cartItems) {
        jdbcTemplate.update(insertSaleSql, saleGroup, userId, item.productId, item.quantity);
        jdbcTemplate.update(updateStockSql, item.quantity, item.productId);
      }

      // 5. Delete all items from the user's cart
      jdbcTemplate.update(deleteCartSql, userId);

      // 6. Query and return the newly created order items
      List<OrderItem> orderItems = jdbcTemplate.query(selectOrderSql, rowMapper, saleGroup);
      return Optional.of(orderItems);

    } catch (CannotCreateOrder e) {
      // Re-throw CannotCreateOrder to let the controller advice handle it and return a 400 Bad Request
      throw e;
    } catch (Exception e) {
      slf4jLogger.error("Error creating order for user " + userId, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<OrderItem>> findAllOrderItemsByUserId(UUID userId) {
    String sql = """
        SELECT 
            s.product_id, 
            p.product_name, 
            p.real_price, 
            p.sale_price, 
            p.slug, 
            s.quantity,
            s.created_at
        FROM sales s
        INNER JOIN products p ON s.product_id = p.id
        WHERE s.user_id = ?
        ORDER BY s.created_at DESC
        """;

    try {
      List<OrderItem> items = jdbcTemplate.query(sql, rowMapper, userId);

      if (items.isEmpty()) {
        return Optional.empty();
      }

      return Optional.of(items);

    } catch (Exception e) {
      slf4jLogger.error("Error finding order items for user " + userId, e);
      return Optional.empty();
    }
  }

  // Helper DTO for mapping cart item with its stock status
  private record CartItemWithStockDto(UUID productId, int quantity, int stock, String productName) {}
}
