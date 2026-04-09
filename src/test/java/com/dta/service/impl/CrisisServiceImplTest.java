package com.dta.service.impl;

import com.dta.ai.CrisisDetector;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.CrisisResponse;
import com.dta.repository.TrustedContactRepository;
import com.dta.service.AiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrisisServiceImplTest {

    @Mock
    private CrisisDetector crisisDetector;

    @Mock
    private TrustedContactRepository trustedContactRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private CrisisServiceImpl crisisService;

    @Test
    void testDetectCrisis_TriggersAlertOnHighRisk() {
        DistortionSuggestionRequest request = new DistortionSuggestionRequest();
        request.setText("I want to end it all");

        CrisisResponse mockResponse = new CrisisResponse();
        mockResponse.setCrisis(true);
        mockResponse.setAction("immediate_intervention");

        when(aiService.detectCrisis(anyString())).thenReturn(mockResponse);

        CrisisResponse response = crisisService.detectCrisis(request);

        assertNotNull(response);
        assertTrue(response.isCrisis());
        assertEquals("immediate_intervention", response.getAction());
        
        verify(aiService).detectCrisis(request.getText());
    }
}