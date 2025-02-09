package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysWorkflowRepository extends JpaRepository<SysWorkflow, String> {
}
