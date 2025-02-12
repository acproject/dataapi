package com.owiseman.dataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KongData{
    @JsonProperty("strip_path")
    private boolean stripPath;

    @JsonProperty("regex_priority")
    private int regexPriority;

    @JsonProperty("snis")
    private Object snis;

    @JsonProperty("response_buffering")
    private boolean responseBuffering;

    @JsonProperty("https_redirect_status_code")
    private int httpsRedirectStatusCode;

    @JsonProperty("tags")
    private Object tags;

    @JsonProperty("path_handling")
    private String pathHandling;

    @JsonProperty("protocols")
    private List<String> protocols;

    @JsonProperty("preserve_host")
    private boolean preserveHost;

    @JsonProperty("sources")
    private Object sources;

    @JsonProperty("service")
    private KongServiceInfo service;

    @JsonProperty("request_buffering")
    private boolean requestBuffering;

    @JsonProperty("destinations")
    private Object destinations;

    @JsonProperty("paths")
    private List<String> paths;

    @JsonProperty("headers")
    private Object headers;

    @JsonProperty("hosts")
    private Object hosts;

    @JsonProperty("methods")
    private List<String> methods;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("updated_at")
    private long updatedAt;

    public boolean isStripPath() {
        return stripPath;
    }

    public void setStripPath(boolean stripPath) {
        this.stripPath = stripPath;
    }

    public int getRegexPriority() {
        return regexPriority;
    }

    public void setRegexPriority(int regexPriority) {
        this.regexPriority = regexPriority;
    }

    public Object getSnis() {
        return snis;
    }

    public void setSnis(Object snis) {
        this.snis = snis;
    }

    public boolean isResponseBuffering() {
        return responseBuffering;
    }

    public void setResponseBuffering(boolean responseBuffering) {
        this.responseBuffering = responseBuffering;
    }

    public int getHttpsRedirectStatusCode() {
        return httpsRedirectStatusCode;
    }

    public void setHttpsRedirectStatusCode(int httpsRedirectStatusCode) {
        this.httpsRedirectStatusCode = httpsRedirectStatusCode;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public String getPathHandling() {
        return pathHandling;
    }

    public void setPathHandling(String pathHandling) {
        this.pathHandling = pathHandling;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    public boolean isPreserveHost() {
        return preserveHost;
    }

    public void setPreserveHost(boolean preserveHost) {
        this.preserveHost = preserveHost;
    }

    public Object getSources() {
        return sources;
    }

    public void setSources(Object sources) {
        this.sources = sources;
    }

    public KongServiceInfo getService() {
        return service;
    }

    public void setService(KongServiceInfo service) {
        this.service = service;
    }

    public boolean isRequestBuffering() {
        return requestBuffering;
    }

    public void setRequestBuffering(boolean requestBuffering) {
        this.requestBuffering = requestBuffering;
    }

    public Object getDestinations() {
        return destinations;
    }

    public void setDestinations(Object destinations) {
        this.destinations = destinations;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public Object getHosts() {
        return hosts;
    }

    public void setHosts(Object hosts) {
        this.hosts = hosts;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }


}
