package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.router.RegisterServiceRequest;
import com.owiseman.dataapi.router.RouteRequest;
import com.owiseman.dataapi.service.JooqService;
import com.owiseman.dataapi.service.KongService;
import com.owiseman.jpa.model.DataRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DynamicQueryController {
    @Autowired
    private KongService kongService;
    @Autowired
    private JooqService jooqService;

    @PostMapping("/register-service")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<?> registerService(@RequestBody RegisterServiceRequest registerServiceRequest)
    {
        kongService.registerService(registerServiceRequest);
        return ResponseEntity.ok("Service registered successfully");
    }

    /**
     * @param routeRequest
     * @return
     */
    @PostMapping("/register-route")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<?> registerRoute(@RequestBody RouteRequest routeRequest)
    {
        kongService.registerRoute(routeRequest);
        return ResponseEntity.ok("Route registered successfully");
    }

    @DeleteMapping("/unregister/{routeNameOrId}")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<?> unregisterRoute(@PathVariable String routeNameOrId)
    {
        kongService.unregisterRoute(routeNameOrId);
        return ResponseEntity.ok("Route unregistered successfully");
    }

    @DeleteMapping("/unregister-service/{serviceNameOrId}")
    @PreAuthorize("hasRole('ADMIN’, 'USER')")
    public ResponseEntity<?> unregisterService(@PathVariable String serviceNameOrId)
    {
        kongService.unregisterService(serviceNameOrId);
        return ResponseEntity.ok("Service unregistered successfully");
    }

    @GetMapping("/get-kong-services")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<String> getKongServices(
            @RequestParam(required = false) String offset,
            @RequestParam(required = false) Integer size) {

        return kongService.getServices(offset, size);
    }

    @GetMapping("/get-kong-routers")
    @PreAuthorize("hasRole('ADMIN‘, 'USER')")
    public ResponseEntity<String> getKongRoutes(
            @RequestParam(required = false) String offset,
            @RequestParam(required = false) Integer size) {

        return kongService.getRoutes(offset, size);
    }

    @RequestMapping(value = "/sql", method = { RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE})
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<?> executeSQL(@RequestBody String json) {
        try {
            DataRecord dataRecord = jooqService.executeSQL(json);
            return ResponseEntity.ok(dataRecord);
        } catch (IllegalArgumentException e) {
            // 参数错误返回400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 其他错误返回500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
