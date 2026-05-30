package com_maboroshi.spring.contexts.catalog.infrastructure.persistence;

import com_maboroshi.spring.contexts.catalog.domain.entities.ArtistDetail;
import com_maboroshi.spring.contexts.catalog.domain.entities.BaseProduct;
import com_maboroshi.spring.contexts.catalog.infrastructure.errors.ProductNotFound;
import com_maboroshi.spring.shared.core.Result;
import com_maboroshi.spring.shared.errors.RepositoryError;
import com_maboroshi.spring.shared.utils.AppLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtistDetailQueries {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper productRowMapper;
    private final AppLogger appLogger;

    public ArtistDetailQueries(
            JdbcTemplate jdbcTemplate,
            ProductRowMapper productRowMapper,
            AppLogger appLogger) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = productRowMapper;
        this.appLogger = appLogger;
    }

    public Result<ArtistDetail, RepositoryError> getArtistDetail(String artistId) {

        try {

            String artistSql = """
                    SELECT
                        id,
                        name,
                        image
                    FROM artists
                    WHERE id = ?
                    """;

            ArtistDetail artist = jdbcTemplate.queryForObject(
                    artistSql,
                    (rs, rowNum) -> new ArtistDetail(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("image"),
                            new BaseProduct[0]),
                    java.util.UUID.fromString(artistId));

            if (artist == null) {
                return Result.fail(new ProductNotFound("Artist not found"));
            }

            String productsSql = """
                    SELECT
                        p.id,
                        p.product_name,
                        p.artist,
                        p.real_price,
                        p.sale_price,
                        p.stock,
                        p.slug,
                        ARRAY(
                            SELECT url
                            FROM product_images
                            WHERE product_id = p.id
                        ) AS images
                    FROM products p
                    WHERE p.artist_id = ?
                      AND p.is_visible = true
                    """;

            List<BaseProduct> products = jdbcTemplate.query(
                    productsSql,
                    productRowMapper,
                    java.util.UUID.fromString(artistId));

            ArtistDetail response = new ArtistDetail(
                    artist.getArtistId(),
                    artist.getName(),
                    artist.getImage(),
                    products.toArray(new BaseProduct[0]));

            return Result.ok(response);

        } catch (DataAccessException e) {
            appLogger.error("Database error fetching artist detail", e);
            return Result.fail(new ProductNotFound(e.getMessage()));
        }
    }
}