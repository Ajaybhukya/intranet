package com.intranet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_sheet_entry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"timesheetId", "taskId", "projectId"})
})
public class TimeSheetEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeSheetEntryId;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private Long timesheetId;

    
    // private Project project; // FK from project management system via API

    // private Task task; // FK from project management system via API

    private Long taskId;
    private Long projectId;

    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String workType = "WFO";

    @Column(precision = 5, scale = 2)
    private BigDecimal hoursWorked;

    private String otherDescription;
}
