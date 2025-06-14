package com.tms.dto;


import lombok.Data;

import java.util.Date;

@Data
public class LogHoursDTO {
    private int contractorId;
    private String projectCode;
    private String activityType;
    private Date date;
    private double hoursWorked;
    private String status; // DRAFT or SUBMITTED
}

