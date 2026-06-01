package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ProductRowMapper implements RowMapper<BaseProduct> {

  @Override
  public BaseProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
    String[] images = (String[]) rs.getArray("images").getArray();
    return new BaseProduct(
        UUID.fromString(rs.getString("id")),
        rs.getString("product_name"),
        rs.getString("artist"),
        rs.getString("artist_image"),
        rs.getDouble("real_price"),
        rs.getDouble("sale_price"),
        rs.getInt("stock"),
        rs.getString("slug"),
        images,
        false,
        false,
        rs.getString("type"),
        rs.getString("status")
    );
  }
}
