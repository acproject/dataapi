//package com.owiseman.dataapi.service;
//
//import com.owiseman.dataapi.dto.PluginDTO;
//import com.owiseman.dataapi.entity.SysPlugin;
//import com.owiseman.dataapi.plugins.PluginStatus;
//import com.owiseman.dataapi.repository.SysPluginRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.time.LocalDateTime;
//
//@Service
//public class PluginService {
//    @Autowired
//    private SysPluginRepository sysPluginRepository;
//
//    @Transactional
//    public void registerPlugin(PluginDTO dto) {
//        SysPlugin entity = sysPluginRepository.findByName(dto.getName())
//            .orElseGet(SysPlugin::new);
//        entity.setName(dto.getName());
//        entity.setPort(dto.getPort());
//        entity.setStatus(PluginStatus.RUNNING);
//        entity.setLastHeartbeat(LocalDateTime.now());
//        sysPluginRepository.save(entity);
//    }
//
//    // 心跳检测更新
//    @Scheduled(fixedRate = 5000)
//    @Transactional
//    public void checkHealth() {
//        sysPluginRepository.findAll().forEach(plugin -> {
//            try (Socket s = new Socket("localhost", plugin.getPort())) {
//                plugin.setStatus(PluginStatus.RUNNING);
//            } catch (IOException e) {
//                plugin.setStatus(PluginStatus.STOPPED);
//            }
//            plugin.setLastHeartbeat(LocalDateTime.now());
//        });
//    }
//}
