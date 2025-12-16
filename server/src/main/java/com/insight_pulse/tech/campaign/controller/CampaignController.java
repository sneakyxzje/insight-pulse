package com.insight_pulse.tech.campaign.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insight_pulse.tech.campaign.dto.CreateCampaignRequest;
import com.insight_pulse.tech.campaign.dto.CreateCampaignResponse;
import com.insight_pulse.tech.campaign.dto.GetCampaignByIdResponse;
import com.insight_pulse.tech.campaign.dto.GetCampaignResponse;
import com.insight_pulse.tech.campaign.service.CampaignService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaign")
public class CampaignController {
    
    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CreateCampaignResponse> createCampaign(@RequestBody CreateCampaignRequest request) {
        return ResponseEntity.ok(campaignService.createCampaign(request));
    }

    @GetMapping
    public ResponseEntity<List<GetCampaignResponse>> getCampaigns() {
        return ResponseEntity.ok(campaignService.getCampaigns());
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<GetCampaignByIdResponse> getCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(campaignService.getCampaignById(campaignId));
    }
}
