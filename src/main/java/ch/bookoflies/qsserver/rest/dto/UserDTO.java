package ch.bookoflies.qsserver.rest.dto;

import java.util.*;

public class UserDTO {

    private String name;

    private String token;

    private  Set<String> authenticationIdentities = new HashSet<>();

    private Set<String> subscribedLibraries = new HashSet<>();

    private Set<String> subscribedArticles = new HashSet<>();

    private Map<String, String> attributes = new HashMap<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getAuthenticationIdentities() {
        return authenticationIdentities;
    }

    public void setAuthenticationIdentities(Set<String> authenticationIdentities) {
        this.authenticationIdentities = authenticationIdentities;
    }

    public Set<String> getSubscribedLibraries() {
        return subscribedLibraries;
    }

    public void setSubscribedLibraries(Set<String> subscribedLibraries) {
        this.subscribedLibraries = subscribedLibraries;
    }

    public Set<String> getSubscribedArticles() {
        return subscribedArticles;
    }

    public void setSubscribedArticles(Set<String> subscribedArticles) {
        this.subscribedArticles = subscribedArticles;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
