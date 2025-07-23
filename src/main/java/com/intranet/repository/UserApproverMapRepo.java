package com.intranet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.entity.UserApproverMap;

public interface UserApproverMapRepo extends JpaRepository<UserApproverMap, Long> {
} 