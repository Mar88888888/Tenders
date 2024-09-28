package com.example.tendersystem.proposal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderProposalRepository extends CrudRepository<TenderProposal, Long> {
}
