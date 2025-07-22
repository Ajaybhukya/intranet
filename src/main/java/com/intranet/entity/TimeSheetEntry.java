package com.intranet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    // @ManyToOne
    // @JoinColumn(name = "timesheet_id")
    // private TimeSheet timesheet; // FK from TimeSheet Enity
    private Long timesheetId;
    // @ManyToOne
    // @JoinColumn(name = "project_id")
    // private Project project;

    // @ManyToOne
    // @JoinColumn(name = "task_id")
    // private Task task;

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
