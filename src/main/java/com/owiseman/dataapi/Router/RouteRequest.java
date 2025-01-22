package com.owiseman.dataapi.Router;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
/**
 * {@inheritDoc}
 * ```json
 * {
 *     "id": "string",
 *     "name": "your-route",
 *     "methods": ["POST", "GET", "PUT", "DELETE"],
 *     upstream_url: "http://your-backend-service:8080/api/query"
 * }
 * ```
 * usage：example
 * ```js
 * const routeName = "your-route-{name}-{id}";
 * fetch(`http://kong-gateway/api/${routeName}` {
 *   method: "POST",
 *   headers: {
 *       'Content-Type': 'application/json'
 *       'Authorization': 'Bearer {token}'
 *   },
 *   body: JSON.stringify({ ... })
 * })
 * .then(response => response.json())
 * .then(data => {console.log(data})
 * .catch(error => {console.error(error)});
 * ```
 */
public class RouteRequest {
    private String id;
    private String name;
    private List<String> uris;
    private List<String> methods;
    private String upstreamUrl;
}
