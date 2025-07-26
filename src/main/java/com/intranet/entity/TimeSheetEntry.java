package com.intranet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_sheet_entry", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"timesheetId", "taskId", "projectId"}))
public class TimeSheetEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timesheetEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheetId")
    private TimeSheet timesheet;

    private Long projectId; // external Project reference
    private Long taskId;    // external tasks reference

    private String description;

    private String workType;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    private BigDecimal hoursWorked; // New field for calculated hours

    private String otherDescription;

    public String getStatus() {
        
        if (timesheet != null) {
            return timesheet.getStatus();
        }else{
            // no timesheet associated
            return "PENDING";
        }
    }
}
