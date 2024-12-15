package com.example.tendersystem.tender;

import com.example.tendersystem.interfaces.tender.TenderDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TenderDaoImpl implements TenderDao {
    private final JdbcTemplate jdbcTemplate;

    public TenderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long create(Tender tender) {
        String sql = "INSERT INTO tender (title, description, created_date, expected_price, is_active, owner_id, keywords, accepted_proposal_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String keywords = String.join(",", tender.getKeywords());
        Long acceptedProposalId = tender.getAcceptedProposalId();
        return jdbcTemplate.queryForObject(sql, Long.class, tender.getTitle(), tender.getDescription(),
                tender.getCreatedDate(), tender.getExpectedPrice(),
                tender.isActive(), tender.getOwnerId(), keywords, acceptedProposalId);
    }

    @Override
    public Optional<Tender> read(Long id) {
        String sql = "SELECT * FROM tender WHERE id = ?";
        List<Tender> tenders = jdbcTemplate.query(sql, new TenderRowMapper(), id);
        return tenders.stream().findFirst();
    }

    @Override
    public void update(Tender tender) {
        String sql = "UPDATE tender SET title = ?, description = ?, expected_price = ?, is_active = ?, owner_id = ?, keywords = ?, accepted_proposal_id = ? WHERE id = ?";
        String keywords = (tender.getKeywords() == null || tender.getKeywords().isEmpty()) ? ""
                : String.join(",", tender.getKeywords());
        Long acceptedProposalId = tender.getAcceptedProposalId();
        jdbcTemplate.update(sql, tender.getTitle(), tender.getDescription(),
                tender.getExpectedPrice(), tender.isActive(), tender.getOwnerId(), keywords, acceptedProposalId,
                tender.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM tender WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Tender> findAll() {
        String sql = "SELECT * FROM tender";
        return jdbcTemplate.query(sql, new TenderRowMapper());
    }

    @Override
    public List<Tender> findFilteredTenders(LocalDate date, Double minSum, Double maxSum, int offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tender WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (date != null) {
            sql.append(" AND created_date = ?");
            params.add(java.sql.Date.valueOf(date));
        }
        if (minSum != null) {
            sql.append(" AND expected_price >= ?");
            params.add(minSum);
        }
        if (maxSum != null) {
            sql.append(" AND expected_price <= ?");
            params.add(maxSum);
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), new TenderRowMapper(), params.toArray());
    }

    @Override
    public List<Tender> findByOwner(Long ownerId) {
        String sql = "SELECT * FROM tender WHERE owner_id = ?";
        return jdbcTemplate.query(sql, new TenderRowMapper(), ownerId);
    }

    @Override
    public List<Tender> findActiveTenders() {
        String sql = "SELECT * FROM tender WHERE is_active = true";
        return jdbcTemplate.query(sql, new TenderRowMapper());
    }

    @Override
    public List<Tender> findByKeywords(List<String> keywords) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tender WHERE ");

        for (int i = 0; i < keywords.size(); i++) {
            sql.append("keywords ILIKE ?");
            if (i < keywords.size() - 1) {
                sql.append(" OR ");
            }
        }

        List<String> likePatterns = keywords.stream()
                .map(keyword -> "%" + keyword + "%")
                .collect(Collectors.toList());

        return jdbcTemplate.query(sql.toString(), new TenderRowMapper(), likePatterns.toArray());
    }

    private static class TenderRowMapper implements RowMapper<Tender> {
        @Override
        public Tender mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Tender tender = new Tender();
            tender.setId(rs.getLong("id"));
            tender.setTitle(rs.getString("title"));
            tender.setDescription(rs.getString("description"));
            tender.setCreatedDate(rs.getDate("created_date"));
            tender.setExpectedPrice(rs.getDouble("expected_price"));
            tender.setActive(rs.getBoolean("is_active"));
            tender.setOwnerId(rs.getLong("owner_id"));

            String keywordsString = rs.getString("keywords");
            if (keywordsString != null) {
                tender.setKeywords(Arrays.asList(keywordsString.split(",")));
            }

            Long acceptedProposalId = rs.getLong("accepted_proposal_id");
            if (rs.wasNull()) {
                tender.setAcceptedProposalId(null);
            } else {
                tender.setAcceptedProposalId(acceptedProposalId);
            }

            return tender;
        }
    }
}
