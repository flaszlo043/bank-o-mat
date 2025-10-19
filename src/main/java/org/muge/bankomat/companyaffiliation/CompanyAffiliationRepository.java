package org.muge.bankomat.companyaffiliation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAffiliationRepository extends JpaRepository<CompanyAffiliation, Long> {
    List<CompanyAffiliation> findByPersonId(Long personId);
}
