package com.insight_pulse.tech.campaign.dto;

public record CampaignStats(
    Integer totalSubmissions,
    Integer activeCampaigns,
    Double highQualityRatio
) {
    
}
