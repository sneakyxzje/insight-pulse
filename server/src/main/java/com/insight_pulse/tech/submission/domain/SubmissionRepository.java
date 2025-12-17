package com.insight_pulse.tech.submission.domain;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findAllByCampaignId(String campaignId);

    @Query("""
        SELECT s
        FROM Submission s
        WHERE s.campaign.id = :campaignId
        AND s.campaign.user.id = :userId
    """)
    List<Submission> findByCampaignAndUser(
        @Param("campaignId") String campaignId,
        @Param("userId") int userId
    );

    long countByCampaign_Id(String campaignId);
}
