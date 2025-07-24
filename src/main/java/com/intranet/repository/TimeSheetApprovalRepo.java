package com.intranet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("SELECT a FROM TimeSheetApproval a WHERE a.approver.approverId = :managerId")
    List<TimeSheetApproval> findByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT new com.intranet.dto.PendingApprovalDTO(" +
       "uam.userId, uam.approverId) " +
       "FROM TimeSheetApproval tsa " +
       "JOIN tsa.approver uam " +
       "WHERE tsa.approvalStatus = 'Pending'")
    List<PendingApprovalDTO> findPendingUserAndManagerPairs();


}
