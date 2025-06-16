package com.tms.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.tms.dto.TimesheetHistoryResponse;
import com.tms.dto.TimesheetReviewRequest;

@Service
public interface TimesheetService {
    String reviewTimesheet(TimesheetReviewRequest request);
    TimesheetHistoryResponse getTimesheetHistory(int contractorId, LocalDateTime startDate, LocalDateTime endDate);


}