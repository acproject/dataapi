//package com.owiseman.dataapi.repository;
//
//import com.owiseman.dataapi.entity.SysPlugin;
//import com.owiseman.dataapi.plugins.PluginStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface SysPluginRepository extends JpaRepository<SysPlugin,String> {
//    Optional<SysPlugin> findByName(String name);
//    List<SysPlugin> findStatus(PluginStatus status);
//}
