package com.intranet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intranet.entity.UserApproverMap;

public interface UserApproverMapRepo extends JpaRepository<UserApproverMap, Long> {

    
    List<UserApproverMap> findByApproverId(Long approverId);
    
    List<UserApproverMap> findByUserId(Long userId);

    Optional<UserApproverMap> findByUserIdAndApproverId(Long userId, Long approverId);

    void deleteByUserIdAndApproverId(Long userId, Long approverId);
}
