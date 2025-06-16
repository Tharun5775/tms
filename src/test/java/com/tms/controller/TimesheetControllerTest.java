package com.tms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tms.dto.TimesheetHistoryResponse;
import com.tms.dto.TimesheetReviewRequest;
import com.tms.dto.TimesheetSummaryDTO;
import com.tms.service.TimesheetService;

class TimesheetControllerTest {

    @InjectMocks
    private TimesheetController timesheetController;

    @Mock
    private TimesheetService timesheetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReviewTimesheet_Success() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(1);
        request.setApprovedBy(1001);
        request.setDecision("APPROVED");
        request.setComments("Reviewed and approved");

        when(timesheetService.reviewTimesheet(request)).thenReturn("APPROVED");

        ResponseEntity<Map<String, Object>> response = timesheetController.reviewTimesheet(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Timesheet reviewed and updated.", response.getBody().get("message"));
        assertEquals("APPROVED", response.getBody().get("decision"));
    }

  
    @Test
    void testReviewTimesheet_ResponseStructure() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(2);
        request.setApprovedBy(1002);
        request.setDecision("REJECTED");
        request.setComments("Missing entries");

        when(timesheetService.reviewTimesheet(request)).thenReturn("REJECTED");

        ResponseEntity<Map<String, Object>> response = timesheetController.reviewTimesheet(request);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.get("statusCode"));
        assertEquals("REJECTED", body.get("decision"));
    }

  
}
