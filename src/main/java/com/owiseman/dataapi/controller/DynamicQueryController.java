package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.Router.RegisterServiceRequest;
import com.owiseman.dataapi.Router.RouteRequest;
import com.owiseman.dataapi.service.JooqService;
import com.owiseman.dataapi.service.KongService;
import com.owiseman.jpa.model.DataRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class DynamicQueryController {
    @Autowired
    private KongService kongService;
    @Autowired
    private JooqService jooqService;

    @PostMapping("/register-service")
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
    // todo:   @PreAuthorize("hasRole('admin')") // todo: add security
    public ResponseEntity<?> registerRoute(@RequestBody RouteRequest routeRequest)
    {
        kongService.registerRoute(routeRequest);
        return ResponseEntity.ok("Route registered successfully");
    }

    @DeleteMapping("/unregister/{routeNameOrId}")
    // todo:   @PreAuthorize("hasRole('admin')") // todo: add security
    public ResponseEntity<?> unregisterRoute(@PathVariable String routeNameOrId)
    {
        kongService.unregisterRoute(routeNameOrId);
        return ResponseEntity.ok("Route unregistered successfully");
    }

    @DeleteMapping("/unregister-service/{serviceNameOrId}")
    public ResponseEntity<?> unregisterService(@PathVariable String serviceNameOrId)
    {
        kongService.unregisterService(serviceNameOrId);
        return ResponseEntity.ok("Service unregistered successfully");
    }

    @RequestMapping(value = "/query", method = { RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<?> executeSQL(@RequestBody String json) {
        try {
            DataRecord dataRecord = jooqService.executeSQL(json);
            return ResponseEntity.ok(dataRecord);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
