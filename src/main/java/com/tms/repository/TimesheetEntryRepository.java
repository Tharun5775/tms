package com.tms.repository;

import com.tms.entity.TimesheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Integer> {
    List<TimesheetEntry> findByContractorContractorId(int contractorId);

    List<TimesheetEntry> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);}
