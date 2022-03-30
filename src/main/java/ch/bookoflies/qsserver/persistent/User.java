package ch.bookoflies.qsserver.persistent;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true, unique = true) //TODO nullable unique?
    private String token;

    @ElementCollection
    private final Set<String> authenticationIdentities = new HashSet<>();

    @ManyToMany
    private final Set<Library> subscribedLibraries = new HashSet<>();

    @ManyToMany
    private final Set<Article> subscribedArticles = new HashSet<>();

    @ElementCollection
    private final Map<String, String> attributes = new HashMap<>();

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addAuthenticationIdentity(String identity) {
        authenticationIdentities.add(identity);
    }

}
