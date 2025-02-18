//package com.owiseman.dataapi.service;
//
//import com.owiseman.dataapi.aop.error.PluginNotFoundException;
//import com.owiseman.dataapi.entity.SysPlugin;
//import com.owiseman.dataapi.plugins.PluginStatus;
//import com.owiseman.dataapi.repository.SysPluginRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Service
//public class WasmPluginEngineService {
//    @Autowired
//    private SysPluginRepository sysPluginRepository;
//
//    public Object executeWasm(String pluginId, String input) {
//        SysPlugin plugin = null;
//        try {
//            plugin = sysPluginRepository.findById(pluginId)
//                    .orElseThrow(() -> new PluginNotFoundException("Plugin not found: " + pluginId));
//        } catch (PluginNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            byte[] wasmBytes = Files.readAllBytes(Paths.get(plugin.getWasmPath()));
////            WasmModule module = WasmModule.parse(wasmBytes);
////            WasmInstance instance = module.instantiate();
////            // 调用WASM函数（需与插件约定接口）
////            WasmFunction function = instance.getFunction("process_data");
////            WasmValue result = function.call(WasmValue.fromString(input));
////            return result.toString();
//        }catch (IOException e) {
//            plugin.setStatus(PluginStatus.ERROR);
//            sysPluginRepository.save(plugin);
//            throw new RuntimeException("WASM加载失败", e);
//        }
//        return null;
//    }
//
//}
