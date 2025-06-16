package com.tms.serviceImpl;

import com.tms.dto.TimesheetReviewRequest;
import com.tms.dto.TimesheetSummaryDTO;
import com.tms.entity.Timesheet;
import com.tms.entity.TimesheetEntry;
import com.tms.exception.ResourceNotFoundException;
import com.tms.repository.TimesheetEntryRepository;
import com.tms.repository.TimesheetRepository;
import com.tms.serviceimpl.TimesheetServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimesheetServiceImplTest {

    @InjectMocks
    private TimesheetServiceImpl timesheetService;

    @Mock
    private TimesheetRepository timesheetRepository;

    @Mock
    private TimesheetEntryRepository timesheetEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReviewTimesheet_ApprovedSuccessfully() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(1);
        request.setDecision("APPROVED");
        request.setComments("Looks good");

        Timesheet timesheet = new Timesheet();
        timesheet.setTimesheetId(1);
        timesheet.setStatus("SUBMITTED");

        when(timesheetRepository.findById(1)).thenReturn(Optional.of(timesheet));

        String result = timesheetService.reviewTimesheet(request);

        assertEquals("APPROVED", result);
        verify(timesheetRepository).save(timesheet);
    }

    @Test
    void testReviewTimesheet_TimesheetNotFound() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(99);
        request.setDecision("APPROVED");

        when(timesheetRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> timesheetService.reviewTimesheet(request));
    }

    @Test
    void testReviewTimesheet_InvalidStatus() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(1);
        request.setDecision("APPROVED");

        Timesheet timesheet = new Timesheet();
        timesheet.setTimesheetId(1);
        timesheet.setStatus("DRAFT");

        when(timesheetRepository.findById(1)).thenReturn(Optional.of(timesheet));

        assertThrows(IllegalStateException.class, () -> timesheetService.reviewTimesheet(request));
    }

    @Test
    void testReviewTimesheet_InvalidDecision() {
        TimesheetReviewRequest request = new TimesheetReviewRequest();
        request.setTimesheetId(1);
        request.setDecision("INVALID");
        request.setComments("N/A");

        Timesheet timesheet = new Timesheet();
        timesheet.setTimesheetId(1);
        timesheet.setStatus("SUBMITTED");

        when(timesheetRepository.findById(1)).thenReturn(Optional.of(timesheet));

        assertThrows(IllegalArgumentException.class, () -> timesheetService.reviewTimesheet(request));
    }

    
    @Test
    void testGetTimesheetHistory_NoTimesheetsFound() {
        when(timesheetRepository.findByContractorContractorId(999)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () ->
                timesheetService.getTimesheetHistory(999, LocalDateTime.now().minusDays(5), LocalDateTime.now()));
    }
}
