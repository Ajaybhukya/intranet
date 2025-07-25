package com.intranet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.dto.ManagerDetailedDTO;
import com.intranet.dto.PendingApprovalDTO;
import com.intranet.entity.TimeSheetApproval;

public interface TimeSheetApprovalRepo extends JpaRepository<TimeSheetApproval, Long> {

    @Query("SELECT a FROM TimeSheetApproval a WHERE a.timesheet.id = ?1")
    List<TimeSheetApproval> findByTimesheetId(Long timesheetId);

     @Query("SELECT a FROM TimeSheetApproval a " +
       "WHERE a.approver.approverId = :managerId " +
       "AND (:status IS NULL OR a.approvalStatus = :status) " +
       "AND (:workDate IS NULL OR a.timesheet.workDate = :workDate)")
List<TimeSheetApproval> findApprovalsForManager(
    @Param("managerId") Long managerId,
    @Param("status") String status,
    @Param("workDate") LocalDate workDate
);
    List<TimeSheetApproval> findByApprover_UserId(Long managerUserId);
    List<TimeSheetApproval> findByTimesheet_Id(Long timesheetId);


    @Query("SELECT new com.intranet.dto.PendingApprovalDTO(" +
       "uam.userId, uam.approverId) " +
       "FROM TimeSheetApproval tsa " +
       "JOIN tsa.approver uam " +
       "WHERE tsa.approvalStatus = 'Pending'")
    List<PendingApprovalDTO> findPendingUserAndManagerPairs();

    @Query("SELECT new com.intranet.dto.PendingApprovalDTO(" +
       "uam.userId, uam.approverId) " +
       "FROM TimeSheetApproval tsa " +
       "JOIN tsa.approver uam " +
       "WHERE (:status IS NULL OR tsa.approvalStatus = :status)")
List<PendingApprovalDTO> findUserAndManagerPairsByStatus(@Param("status") String status);



@Query("""
    SELECT new com.intranet.dto.ManagerDetailedDTO(
        ts.userId,
        ts.id,
        e.projectId,
        e.taskId,
        e.hoursWorked,
        a.approvalStatus,
        e.description,
        ts.workDate
    )
    FROM TimeSheetApproval a
    JOIN a.timesheet ts
    JOIN ts.entries e
    JOIN a.approver uam
    WHERE uam.approverId = :managerId
""")
List<ManagerDetailedDTO> findDetailedByManagerId(@Param("managerId") Long managerId);


@Query("SELECT a FROM TimeSheetApproval a " +
       "WHERE a.approver.approverId = :managerId " +
       "AND a.timesheet.workDate BETWEEN :startDate AND :endDate")
List<TimeSheetApproval> findByManagerIdAndDateRange(
    @Param("managerId") Long managerId,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate
);

@Query("SELECT a FROM TimeSheetApproval a " +
       "WHERE a.approver.approverId = :managerId " +
       "AND a.approvalStatus = :status " +
       "AND a.timesheet.workDate BETWEEN :startDate AND :endDate")
List<TimeSheetApproval> findByManagerIdAndStatusAndDateRange(
    @Param("managerId") Long managerId,
    @Param("status") String status,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate
);


@Query("SELECT a FROM TimeSheetApproval a WHERE a.approver.approverId = :managerId AND a.approvalStatus = :status")
List<TimeSheetApproval> findByManagerIdAndStatus(@Param("managerId") Long managerId,
                                                 @Param("status") String status);


}
