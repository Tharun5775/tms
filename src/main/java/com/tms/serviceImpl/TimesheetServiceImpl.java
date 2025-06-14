package com.tms.serviceImpl;

import java.time.Instant;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.temporal.ChronoUnit;

import java.util.List;

import java.util.stream.Collectors;

import com.tms.dto.TimesheetReviewRequest;

import com.tms.dto.TimesheetSummaryDTO;

import com.tms.entity.Timesheet;

import com.tms.entity.TimesheetEntry;

import com.tms.exception.ResourceNotFoundException;

import com.tms.repository.TimesheetEntryRepository;

import com.tms.repository.TimesheetRepository;

import com.tms.service.TimesheetService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private TimesheetEntryRepository timesheetEntryRepository;

    @Override

    public String reviewTimesheet(TimesheetReviewRequest request) {

        // Fetch the timesheet using timesheetId

        Timesheet timesheet = timesheetRepository.findById(request.getTimesheetId())

                .orElseThrow(() -> {

                    log.error("Timesheet not found for timesheetId: {}", request.getTimesheetId());

                    return new ResourceNotFoundException("Timesheet not found for timesheetId: " + request.getTimesheetId());

                });

        // Only allow review if current status is SUBMITTED

        if (!"SUBMITTED".equalsIgnoreCase(timesheet.getStatus())) {

            log.warn("Cannot review timesheetId {} - current status: {}", request.getTimesheetId(), timesheet.getStatus());

            throw new IllegalStateException("Only timesheets with status SUBMITTED can be approved or rejected.");

        }

        // Validate decision

        String decision = request.getDecision().toUpperCase();

        if (!decision.equals("APPROVED") && !decision.equals("REJECTED")) {

            log.error("Invalid decision '{}' for timesheetId: {}", decision, request.getTimesheetId());

            throw new IllegalArgumentException("Decision must be APPROVED or REJECTED.");

        }

        // Update status and manager comment

        timesheet.setStatus(decision);

        timesheet.setManagerComment(request.getComments());

        timesheetRepository.save(timesheet);

        log.info("TimesheetId {} reviewed successfully with decision: {}", request.getTimesheetId(), decision);

        return decision;

    }

    @Override

    public List<TimesheetSummaryDTO> getTimesheetHistory(int contractorId, LocalDateTime startDate, LocalDateTime endDate) {

        List<Timesheet> timesheets = timesheetRepository.findByContractorContractorId(contractorId);

        if (timesheets.isEmpty()) {

            log.warn("No timesheets found for contractor ID: {}", contractorId);

            throw new ResourceNotFoundException("No timesheets found for contractor ID: " + contractorId);

        }

        log.info("Fetching APPROVED timesheets for contractor ID: {} between {} and {}", contractorId, startDate,

                endDate);

        return timesheets.stream().filter(ts -> {


            LocalDateTime weekStart = ts.getWeekStartDate().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return "APPROVED".equalsIgnoreCase(ts.getStatus())
                    && (weekStart.isEqual(startDate) || weekStart.isAfter(startDate))
                    && (weekStart.isEqual(endDate) || weekStart.isBefore(endDate));

        }).map(ts -> {

            TimesheetEntry entry = ts.getTimesheetEntry();

            TimesheetSummaryDTO dto = new TimesheetSummaryDTO();

            dto.setTimesheetId(ts.getTimesheetId());

            dto.setWeekStart(ts.getWeekStartDate());

            ZoneId zoneId = ZoneId.systemDefault();
            Instant weekStartInstant = ts.getWeekStartDate().atZone(zoneId).toInstant();
            Instant weekEndInstant = weekStartInstant.plus(6, ChronoUnit.DAYS);
            LocalDateTime weekEnd = LocalDateTime.ofInstant(weekEndInstant, zoneId);
            dto.setWeekEnd(weekEnd);


            dto.setStatus(ts.getStatus());

            dto.setTotalHours(entry.getHoursWorked());

            return dto;

        }).collect(Collectors.toList());

    }

}

