package com.tms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.entity.Timesheet;
import org.springframework.stereotype.Repository;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {
    // Find timesheets by contractor's ID
    List<Timesheet> findByContractorContractorId(int contractorId);

    Optional<Timesheet> findByTimesheetId(int entryId);

    // Optional: Fetch all timesheets of a contractor
    // Optional: Fetch timesheets by status
    List<Timesheet> findByStatus(String status);

}