package com.intranet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.dto.TeamTimeSheetDTO;
import com.intranet.entity.TimeSheet;

public interface TimeSheetRepo extends JpaRepository<TimeSheet, Long> {

    List<TimeSheet> findByUserIdOrderByWorkDateDesc(Long userId);


    @Query("SELECT new com.intranet.dto.TeamTimeSheetDTO(t.id, t.userId, t.workDate) " +
           "FROM TimeSheet t " +
           "WHERE EXISTS (" +
           "   SELECT 1 FROM UserApproverMap m " +
           "   WHERE m.userId = t.userId AND m.approverId = :managerId" +
           ") " +
           "AND (:workDate IS NULL OR t.workDate = :workDate)")
    List<TeamTimeSheetDTO> findTeamTimeSheetsByManager(
        @Param("managerId") Long managerId,
        @Param("workDate") LocalDate workDate
    );

}
