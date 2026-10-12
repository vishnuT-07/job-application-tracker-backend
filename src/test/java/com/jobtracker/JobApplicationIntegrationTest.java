package com.jobtracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.dto.InterviewRequest;
import com.jobtracker.dto.JobApplicationRequest;
import com.jobtracker.enums.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end REST API integration tests.
 * CSE Viva Note: @SpringBootTest spins up the Spring application context,
 * and MockMvc simulates HTTP requests and validates JSON responses without needing a live browser.
 */
@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompleteApplicationAndInterviewWorkflow() throws Exception {
        // 1. Create a new Job Application
        JobApplicationRequest newApp = new JobApplicationRequest(
                "Google",
                "Software Engineer",
                LocalDate.now(),
                "https://careers.google.com/jobs/123",
                "Applied through referral",
                ApplicationStatus.APPLIED
        );

        String createResponse = mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newApp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.companyName", is("Google")))
                .andExpect(jsonPath("$.jobTitle", is("Software Engineer")))
                .andExpect(jsonPath("$.status", is("APPLIED")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long appId = objectMapper.readTree(createResponse).get("id").asLong();

        // 2. Schedule an interview for this application
        InterviewRequest interviewReq = new InterviewRequest(
                appId,
                LocalDateTime.now().plusDays(3),
                "Technical Screening",
                "Online Video (Google Meet)",
                "Focus on DSA and Algorithms"
        );

        mockMvc.perform(post("/api/applications/" + appId + "/interviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interviewReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.round", is("Technical Screening")))
                .andExpect(jsonPath("$.type", is("Online Video (Google Meet)")));

        // 3. Verify application status was automatically transitioned to INTERVIEW
        mockMvc.perform(get("/api/applications/" + appId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INTERVIEW")))
                .andExpect(jsonPath("$.interviews", hasSize(1)));

        // 4. Update status to OFFER
        mockMvc.perform(patch("/api/applications/" + appId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "OFFER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("OFFER")));

        // 5. Test search and filtering
        mockMvc.perform(get("/api/applications")
                        .param("keyword", "Google")
                        .param("status", "OFFER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].companyName", is("Google")));

        // 6. Test dashboard stats endpoint
        mockMvc.perform(get("/api/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalApplications", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.offers", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.statusBreakdown.OFFER", greaterThanOrEqualTo(1)));

        // 7. Delete the application
        mockMvc.perform(delete("/api/applications/" + appId))
                .andExpect(status().isNoContent());

        // 8. Verify it is no longer found (404)
        mockMvc.perform(get("/api/applications/" + appId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFormValidationReturns400() throws Exception {
        // Missing companyName and jobTitle
        JobApplicationRequest invalidReq = new JobApplicationRequest(
                "",
                "",
                LocalDate.now(),
                null,
                null,
                ApplicationStatus.APPLIED
        );

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Failed")))
                .andExpect(jsonPath("$.errors.companyName", notNullValue()))
                .andExpect(jsonPath("$.errors.jobTitle", notNullValue()));
    }
}
