package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysAuditLogRepository extends JpaRepository<SysAuditLog, String> {
}
