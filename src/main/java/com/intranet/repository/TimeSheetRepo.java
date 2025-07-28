package com.intranet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.TimeSheet;

public interface TimeSheetRepo extends JpaRepository<TimeSheet, Long> {

    List<TimeSheet> findByUserIdOrderByWorkDateDesc(Integer userId);
}
