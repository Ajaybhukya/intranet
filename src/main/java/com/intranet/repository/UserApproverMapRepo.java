package com.intranet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.intranet.entity.UserApproverMap;

public interface UserApproverMapRepo extends JpaRepository<UserApproverMap, Long> {

    
    List<UserApproverMap> findByApproverId(Long approverId);
    List<UserApproverMap> findByUserId(Long userId);


}
