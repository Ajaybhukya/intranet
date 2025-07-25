package com.intranet.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.dto.TeamTimeSheetDTO;
import com.intranet.entity.TimeSheet;

public interface TimeSheetRepo extends JpaRepository<TimeSheet, Long> {

    List<TimeSheet> findByUserIdOrderByWorkDateDesc(Long userId);

    Optional<TimeSheet> findByUserIdAndWorkDate(Long userId, LocalDate workDate);

    
}
