package com.owiseman.dataapi.dto;


import java.util.List;

public class LookupResponse {
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public LookupResponse() {
    }

    public LookupResponse(List<Location> locations) {
        this.locations = locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public static class Location {
        private String publicUrl;
        private String url;

        public String getPublicUrl() {
            return publicUrl;
        }

        public void setPublicUrl(String publicUrl) {
            this.publicUrl = publicUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Location(String publicUrl, String url) {
            this.publicUrl = publicUrl;
            this.url = url;
        }
        public Location() {
        }
    }
}
