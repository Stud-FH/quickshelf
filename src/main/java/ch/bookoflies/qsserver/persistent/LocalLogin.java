package ch.bookoflies.qsserver.persistent;

import javax.persistence.*;

@Entity
@Table(name = "local_login")
public class LocalLogin {

    @Id
    private String id;

    @OneToOne(optional = false)
    private User user;

    @Transient
    private String password;

    @Column(nullable = false)
    private String passwordEncoded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordEncoded() {
        return passwordEncoded;
    }

    public void setPasswordEncoded(String passwordEncoding) {
        this.passwordEncoded = passwordEncoding;
    }
}
