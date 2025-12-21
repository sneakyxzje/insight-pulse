package com.insight_pulse.tech.campaign.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.insight_pulse.tech.campaign.domain.Campaign;
import com.insight_pulse.tech.campaign.domain.CampaignRepository;
import com.insight_pulse.tech.campaign.domain.CampaignStatus;
import com.insight_pulse.tech.campaign.dto.CampaignDetailResponse;
import com.insight_pulse.tech.campaign.dto.CampaignRequest;
import com.insight_pulse.tech.campaign.dto.CampaignResponse;
import com.insight_pulse.tech.campaign.dto.CampaignWithSubmissionsResponse;
import com.insight_pulse.tech.campaign.dto.UpdateCampaignRequest;
import com.insight_pulse.tech.security.context.CurrentUserProvider;
import com.insight_pulse.tech.submission.domain.Submission;
import com.insight_pulse.tech.submission.domain.SubmissionRepository;
import com.insight_pulse.tech.submission.dto.SubmissionDetailResponse;
import com.insight_pulse.tech.submission.dto.SubmissionResponse;
import com.insight_pulse.tech.user.domain.User;
import com.insight_pulse.tech.user.domain.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignService {

    
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final CurrentUserProvider currentUserProvider;

    public CampaignResponse createCampaign(CampaignRequest request) {
        int userId = currentUserProvider.getCurrentUserId();
        User user = userRepository.getReferenceById(userId);
        Campaign campaign = new Campaign();
        campaign.setName(request.name());
        campaign.setDescription(request.description());
        campaign.setAiSystemPrompt(request.aiSystemPrompt());
        campaign.setFormSchema(request.formSchema());
        campaign.setUser(user);
        Campaign saved = campaignRepository.save(campaign);
        CampaignStatus status = saved.getStatus();

        return new CampaignResponse(
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            status,
            saved.getCreatedAt()
        );
    }

    public Page<CampaignResponse> getCampaigns(Pageable pageable) {
        int userId = currentUserProvider.getCurrentUserId();
        Page<Campaign> campaigns = campaignRepository.findAllByUserId(userId, pageable);
        return campaigns.map(campaign -> new CampaignResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getStatus(),
            campaign.getCreatedAt()
        ));
    }

    public CampaignDetailResponse getCampaignById(String campaignId) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId)
        .orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));
        long totalSubmissions = submissionRepository.countByCampaign_Id(campaignId);
        return new CampaignDetailResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getStatus(),
            campaign.getFormSchema(),
            campaign.getAiSystemPrompt(),
            campaign.getCreatedAt(),
            campaign.getUpdatedAt(),
            totalSubmissions
        );
    }

    public CampaignWithSubmissionsResponse getSubmissionByCampaign(String campaignId) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId).orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));
        List<Submission> submissions = submissionRepository.findAllByCampaignId(campaignId);
        long totalSubmissions = submissions.size();
        CampaignDetailResponse campaignResponse = new CampaignDetailResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getStatus(),
            campaign.getFormSchema(),
            campaign.getAiSystemPrompt(),
            campaign.getCreatedAt(),
            campaign.getUpdatedAt(),
            totalSubmissions
        );
        List<SubmissionResponse> submissionResponse = submissions.stream().map(s -> new SubmissionResponse(
            s.getId(),
            s.getAiAssessment(),
            s.getAnswers(),
            s.getScore(),
            s.getSubmittedAt()
        )).toList();
        return new CampaignWithSubmissionsResponse(
            campaignResponse,
            submissionResponse
        );
    }

    public SubmissionDetailResponse getSubmissionDetailByCampaign(String campaignId, String submissionId) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId).orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));
        String userPrompts = campaign.getAiSystemPrompt();
        Submission submission = submissionRepository.findSubmissionDetail(userId, campaignId, submissionId);
        return new SubmissionDetailResponse(
            userPrompts,
            submission.getId(),
            submission.getAiAssessment(),
            submission.getAnswers(),
            submission.getScore(),
            submission.getSubmittedAt(),
            submission.getSchemaSnapshot()
        );
    }

    public void toggleCampaignStatus(String campaignId, Boolean enabled) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId).orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));
        if(Boolean.TRUE.equals(enabled)) {
            campaign.setStatus(CampaignStatus.ACTIVE);
        } else {
            campaign.setStatus(CampaignStatus.INACTIVE);
        }
        campaignRepository.save(campaign); 
    }

    @Transactional
    public CampaignDetailResponse updateCampaign(String campaignId, UpdateCampaignRequest request) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId)
        .orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));

        campaign.setName(request.name());
        campaign.setDescription(request.description());
        campaign.setFormSchema(request.formSchema());
        campaign.setAiSystemPrompt(request.aiSystemPrompt());
        campaignRepository.save(campaign);


        return new CampaignDetailResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getStatus(),
            campaign.getFormSchema(),
            campaign.getAiSystemPrompt(),
            campaign.getCreatedAt(),
            campaign.getUpdatedAt(),
            campaign.getTotalSubmissions()
        );
    }

    public CampaignWithSubmissionsResponse findSubmissionByCampaign(String campaignId, String search) {
        int userId = currentUserProvider.getCurrentUserId();
        Campaign campaign = campaignRepository.findByIdAndUserId(campaignId, userId).orElseThrow(() -> new RuntimeException("Campaign not found or permission denied"));
        List<Submission> submissions;

        if(search != null && !search.trim().isEmpty()) {
            submissions = submissionRepository.searchSubmission(campaignId, search.trim());
        }
        else {
            submissions = submissionRepository.findAllByCampaignId(campaignId);
        }
        CampaignDetailResponse campaignResponse = new CampaignDetailResponse(
            campaign.getId(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getStatus(),
            campaign.getFormSchema(),
            campaign.getAiSystemPrompt(),
            campaign.getCreatedAt(),
            campaign.getUpdatedAt(),
            campaign.getTotalSubmissions()
        );
        List<SubmissionResponse> submissionResponse = submissions.stream().map(s -> new SubmissionResponse(
            s.getId(),
            s.getAiAssessment(),
            s.getAnswers(),
            s.getScore(),
            s.getSubmittedAt()
        )).toList();
        return new CampaignWithSubmissionsResponse(
            campaignResponse,
            submissionResponse
        );
    }
}
