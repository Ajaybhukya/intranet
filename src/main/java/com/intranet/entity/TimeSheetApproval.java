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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TimeSheetApproval", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"timesheetId", "userApproverMapId"}))
public class TimeSheetApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeSheetApprovalId;

    @ManyToOne
    @JoinColumn(name = "timesheetId")
    private TimeSheet timesheet;

    @ManyToOne
    @JoinColumn(name = "userApproverMapId")
    private UserApproverMap approver;

    @Column(nullable = false)
    private String approvalStatus = "PENDING";

    private String description;

    @Column(nullable = false)
    private LocalDateTime approvalTime = LocalDateTime.now();

}
