package fh.server.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Account implements Serializable {

    public static final Integer CLEARANCE_LEVEL_UNVERIFIED = -1;    // no exclusive privileges
    public static final Integer CLEARANCE_LEVEL_CUSTOMER = 0;       // required to place orders
    public static final Integer CLEARANCE_LEVEL_EMPLOYEE = 1;       // required to modify orders
    public static final Integer CLEARANCE_LEVEL_CHEF = 2;           // required to modify assortment
    public static final Integer CLEARANCE_LEVEL_ADMIN = 3;          // required to modify accounts

    @Id
    @GeneratedValue
    private Long id; // used for most identification procedures

    @Column(unique = true, nullable = false)
    private String email; // used for identification in login

    @Transient
    private transient String password; // not saved in DB

    @Column(nullable = false)
    private Long passwordHash; // saved in DB instead of password

    @Column(nullable = false)
    private String token; // used to verify an account. Refreshed with each login.

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Integer clearanceLevel = CLEARANCE_LEVEL_CUSTOMER; // TODO implement account verification, then init new accounts with UNVERIFIED


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(Long passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getClearanceLevel() {
        return clearanceLevel;
    }

    public void setClearanceLevel(Integer clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
    }
}
