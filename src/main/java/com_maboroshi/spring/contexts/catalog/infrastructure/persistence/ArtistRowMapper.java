package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.Artist;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ArtistRowMapper implements RowMapper<Artist> {

  @Override
  public Artist mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Artist(
        UUID.fromString(rs.getString("id")),
        rs.getString("name"),
        rs.getString("image")
    );
  }
}