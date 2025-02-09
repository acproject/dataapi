package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysPlugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPluginRepository extends JpaRepository<SysPlugin,String> {
}
