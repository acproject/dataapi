package com.owiseman.dataapi.Router;

import java.util.List;


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
//    private String id;
    private String name;
    private List<String> paths;
    private Boolean strip_path;
    private List<String> protocols;
    private List<String> methods;
    private String service;

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public Boolean getStrip_path() {
        return strip_path;
    }

    public void setStrip_path(Boolean strip_path) {
        this.strip_path = strip_path;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }
}
