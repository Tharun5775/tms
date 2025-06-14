package com.tms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class TimesheetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int entryId;

    @ManyToOne
    @JoinColumn(name = "contractorId")
    private Contractor contractor;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "activityCode")
    private Activity activity;

    private LocalDateTime date;
    private double hoursWorked;
    private String comments;

}
