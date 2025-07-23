package com.intranet.entity;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
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
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "time_sheet_approval", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"timesheetId", "approversId"})
})
public class TimeSheetApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeSheetApprovalId;

    @ManyToOne
    @JoinColumn(name = "timesheet_id", unique = true)
    private TimeSheet timesheet;

    private Long timesheetId;

    @ManyToOne
    @JoinColumn(name = "approvers_id")
    private Approver approver;

    private Long approverId;

    @Column(nullable = false)
    private String approvalStatus = "Pending";

    private String description;

    @CreationTimestamp
    private LocalDateTime approvalTime;

    // Getters, setters
}
