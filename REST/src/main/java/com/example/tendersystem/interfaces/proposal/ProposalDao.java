package com.example.tendersystem.interfaces.proposal;

import com.example.tendersystem.interfaces.GenericDao;
import com.example.tendersystem.proposal.Proposal;

import java.util.List;

public interface ProposalDao extends GenericDao<Proposal, Long> {
  List<Proposal> findByTender(Long tenderId);

  List<Proposal> findByProposer(Long userId);
}
