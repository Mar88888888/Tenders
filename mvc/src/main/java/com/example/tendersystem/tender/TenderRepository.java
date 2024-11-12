package com.example.tendersystem.tender;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderRepository extends CrudRepository<Tender, Long> {
}
