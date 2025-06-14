package com.tms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timesheetId;

    @OneToOne
    @JoinColumn(name="entryId")
    private  TimesheetEntry timesheetEntry;
    private Date weekStartDate;

    @ManyToOne
    @JoinColumn(name = "contractorId")
    private Contractor contractor;

    private String status; // DRAFT, SUBMITTED, APPROVED, REJECTED
    private String managerComment;

}
