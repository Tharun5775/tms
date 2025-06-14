package com.tms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.tms.dto.TimesheetReviewRequest;
import com.tms.dto.TimesheetSummaryDTO;
import org.springframework.stereotype.Service;

@Service
public interface TimesheetService {
    String reviewTimesheet(TimesheetReviewRequest request);
    List<TimesheetSummaryDTO> getTimesheetHistory(int contractorId, LocalDateTime startDate, LocalDateTime endDate);


}