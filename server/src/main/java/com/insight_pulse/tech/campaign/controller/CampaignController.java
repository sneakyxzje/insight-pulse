package com.insight_pulse.tech.campaign.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insight_pulse.tech.campaign.dto.CampaignDetailResponse;
import com.insight_pulse.tech.campaign.dto.CampaignRequest;
import com.insight_pulse.tech.campaign.dto.CampaignResponse;
import com.insight_pulse.tech.campaign.dto.CampaignWithSubmissionsResponse;
import com.insight_pulse.tech.campaign.service.CampaignService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns")
public class CampaignController {
    
    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@RequestBody CampaignRequest request) {
        return ResponseEntity.ok(campaignService.createCampaign(request));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> getCampaigns() {
        return ResponseEntity.ok(campaignService.getCampaigns());
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignDetailResponse> getCampaignById(@PathVariable String campaignId) {
        return ResponseEntity.ok(campaignService.getCampaignById(campaignId));
    }

    @GetMapping("/{campaignId}/submissions")
    public ResponseEntity<CampaignWithSubmissionsResponse> getSubmissionsByCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(campaignService.getSubmissionByCampaign(campaignId));
    }
}
