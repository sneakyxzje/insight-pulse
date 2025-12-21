package com.insight_pulse.tech.campaign.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CampaignRepository extends JpaRepository<Campaign, String> {
    
    Page<Campaign> findAllByUserId(int userId, Pageable pageable);

    Optional<Campaign> findByIdAndUserId(String id, int userId);

    @Modifying 
    @Query("UPDATE Campaign c SET c.totalSubmissions = c.totalSubmissions + 1 WHERE c.id = :id")
    void incrementTotalSubmissions(@Param("id") String id);
}
