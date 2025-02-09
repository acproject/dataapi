package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDataSourceRepository extends JpaRepository<SysDataSource, String> {
}
