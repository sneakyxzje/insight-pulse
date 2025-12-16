package com.insight_pulse.tech.submission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insight_pulse.tech.submission.dto.SubmissionRequest;
import com.insight_pulse.tech.submission.service.SubmissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission")
public class SubmissionController {
 
    private final SubmissionService submissionService;
    @PostMapping("/{campaignId}")
    public ResponseEntity<String> submitForm(@PathVariable String campaignId, @RequestBody SubmissionRequest request) {
        submissionService.submitForm(campaignId, request);
        return ResponseEntity.ok("Submit thành công");
    }
}
