package com.tms.serviceImpl;

import com.tms.dto.LogHoursDTO;
import com.tms.entity.*;
import com.tms.exception.BadRequestException;
import com.tms.exception.ResourceNotFoundException;
import com.tms.repository.*;
import com.tms.serviceimpl.LogHoursServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogHoursServiceImplTest {

    @Mock
    private ContractorRepository contractorRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private TimesheetEntryRepository timesheetEntryRepository;

    @InjectMocks
    private LogHoursServiceImpl logHoursService;

    private LogHoursDTO validDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validDto = new LogHoursDTO();
        validDto.setProjectCode("PRJ001");
        validDto.setActivityType("DEV");
        validDto.setDate(LocalDateTime.now());
        validDto.setHoursWorked(8);
        validDto.setStatus("DRAFT");
    }


    @Test
    void testLogHours_InvalidHours() {
        validDto.setHoursWorked(25);
        assertThrows(BadRequestException.class, () -> logHoursService.logHours(validDto));
    }

    @Test
    void testLogHours_ContractorNotFound() {
        when(contractorRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> logHoursService.logHours(validDto));
    }

    @Test
    void testLogHours_ProjectNotFound() {
        Contractor contractor = new Contractor();
        when(contractorRepository.findById(1)).thenReturn(Optional.of(contractor));
        when(projectRepository.findByProjectCode("PRJ001")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> logHoursService.logHours(validDto));
    }

    @Test
    void testLogHours_ActivityNotFound() {
        Contractor contractor = new Contractor();
        Project project = new Project();
        when(contractorRepository.findById(1)).thenReturn(Optional.of(contractor));
        when(projectRepository.findByProjectCode("PRJ001")).thenReturn(project);
        when(activityRepository.findById("DEV")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> logHoursService.logHours(validDto));
    }
}

