package com.insight_pulse.tech.submission.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, String> {
    
}
