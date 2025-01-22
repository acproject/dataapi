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
    private String id;
    private String name;
    private List<String> uris;
    private List<String> methods;
    private String upstreamUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getUpstreamUrl() {
        return upstreamUrl;
    }

    public void setUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
    }
}
