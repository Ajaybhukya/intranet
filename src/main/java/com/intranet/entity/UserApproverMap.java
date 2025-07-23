package com.intranet.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserApproverMap")
public class UserApproverMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userApproverMapId;
    
    private Long userId;     
    private Long approverId; 

    private String rolePriority;

     @OneToMany(mappedBy = "approver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSheetApproval> approvals = new ArrayList<>();
}

