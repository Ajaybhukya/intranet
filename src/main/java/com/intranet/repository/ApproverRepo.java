package com.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.Approver;

public interface ApproverRepo extends JpaRepository<Approver, Long> {
    
}
