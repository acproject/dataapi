package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.Router.RouteRequest;
import com.owiseman.dataapi.service.JooqService;
import com.owiseman.dataapi.service.KongService;
import com.owiseman.jpa.model.DataRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DynamicQueryController {
    private KongService kongService;
    private JooqService jooqService;

    /**
     * Register a route
     * ```shell
     *  curl -X POST http://localhost:8001/routes \
     *     --data "name=dynamic-route" \
     *     --data "uris[]=/api/query" \
     *     --data "methods[]=POST" \
     *     --data "upstream_url=http://your-backend-service/api/query"
     * ```
     *
     * @param routeRequest
     * @return
     */
    @PostMapping("/register")
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

    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST,
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