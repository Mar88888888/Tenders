package com.example.tendersystem.proposal;

import com.example.tendersystem.interfaces.proposal.ProposalDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProposalDaoImpl implements ProposalDao {
  private final JdbcTemplate jdbcTemplate;

  public ProposalDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Long create(Proposal proposal) {
    String sql = "INSERT INTO proposal (description, submitted_date, price, tender_id, user_id, status) " +
        "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    return jdbcTemplate.queryForObject(sql, Long.class,
        proposal.getDescription(),
        proposal.getSubmittedDate(),
        proposal.getPrice(),
        proposal.getTenderId(),
        proposal.getProposerId(),
        ProposalStatus.PENDING.name());
  }

  @Override
  public Optional<Proposal> read(Long id) {
    String sql = "SELECT * FROM proposal WHERE id = ?";
    List<Proposal> proposals = jdbcTemplate.query(sql, new ProposalRowMapper(), id);
    return proposals.stream().findFirst();
  }

  @Override
  public void update(Proposal proposal) {
    String sql = "UPDATE proposal SET description = ?, price = ?, status = ? WHERE id = ?";
    jdbcTemplate.update(sql, proposal.getDescription(), proposal.getPrice(), proposal.getStatus().name(),
        proposal.getId());
  }

  @Override
  public void delete(Long id) {
    String sql = "DELETE FROM proposal WHERE id = ?";
    jdbcTemplate.update(sql, id);
  }

  @Override
  public List<Proposal> findByTender(Long tenderId) {
    String sql = """
        SELECT *
        FROM proposal p
        WHERE p.tender_id = ?
        """;
    return jdbcTemplate.query(sql, new ProposalRowMapper(), tenderId);
  }

  @Override
  public List<Proposal> findAll() {
    String sql = "SELECT * FROM proposal";
    return jdbcTemplate.query(sql, new ProposalRowMapper());
  }

  @Override
  public List<Proposal> findByProposer(Long userId) {
    String sql = """
        SELECT *
        FROM proposal p
        WHERE p.user_id = ?
        """;
    return jdbcTemplate.query(sql, new ProposalRowMapper(), userId);
  }

  private static class ProposalRowMapper implements RowMapper<Proposal> {
    @Override
    public Proposal mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
      Proposal proposal = new Proposal();
      proposal.setId(rs.getLong("id"));
      proposal.setDescription(rs.getString("description"));
      proposal.setSubmittedDate(rs.getTimestamp("submitted_date").toLocalDateTime());
      proposal.setPrice(rs.getBigDecimal("price"));
      proposal.setTenderId(rs.getLong("tender_id"));
      proposal.setProposerId(rs.getLong("user_id"));
      proposal.setStatus(rs.getString("status") == null ? ProposalStatus.PENDING
          : ProposalStatus
              .valueOf(rs.getString("status")));

      return proposal;
    }
  }
}
