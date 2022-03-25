package ch.bookoflies.qsserver.persistent;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    private String name;

    @ElementCollection
    private final Set<String> authenticationIdentities = new HashSet<>();

//    @Column(unique = true, nullable = false)
//    private String email; // used for identification in login

    @Column(nullable = false)
    private String token;


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

    public void addAuthenticationIdentity(String identity) {
        authenticationIdentities.add(identity);
    }

    //    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
