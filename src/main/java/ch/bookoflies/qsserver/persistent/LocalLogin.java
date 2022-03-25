package ch.bookoflies.qsserver.persistent;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "local_login")
public class LocalLogin {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id; // used for most identification procedures

    @OneToOne()
    private User user;

    @Column(nullable = false)
    private String passwordEncoding;

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

    public String getPasswordEncoding() {
        return passwordEncoding;
    }

    public void setPasswordEncoding(String passwordEncoding) {
        this.passwordEncoding = passwordEncoding;
    }
}
