package com_maboroshi.spring.contexts.complaints.persistence;

import com_maboroshi.spring.contexts.complaints.models.Complaint;
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
public class JdbcComplaintRepository implements ComplaintRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Slf4jLogger slf4jLogger;

    private final RowMapper<Complaint> rowMapper = (rs, rowNum) ->
        Complaint.builder()
            .id(UUID.fromString(rs.getString("id")))
            .fullName(rs.getString("full_name"))
            .dni(rs.getString("dni"))
            .email(rs.getString("email"))
            .phone(rs.getString("phone"))
            .complaintType(rs.getString("complaint_type"))
            .detail(rs.getString("detail"))
            .expectedSolution(rs.getString("expected_solution"))
            .build();

    @Override
    public Optional<Complaint> saveComplaint(Complaint complaint) {

        String sql = """
            INSERT INTO complaints (
                full_name,
                dni,
                email,
                phone,
                complaint_type,
                detail,
                expected_solution
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING *
            """;

        try {

            List<Complaint> result = jdbcTemplate.query(
                sql,
                rowMapper,

                complaint.getFullName(),
                complaint.getDni(),
                complaint.getEmail(),
                complaint.getPhone(),
                complaint.getComplaintType(),
                complaint.getDetail(),
                complaint.getExpectedSolution()
            );

            return result.stream().findFirst();

        } catch (Exception e) {

            slf4jLogger.error(
                "Saving complaint",
                e
            );

            return Optional.empty();
        }
    }
}