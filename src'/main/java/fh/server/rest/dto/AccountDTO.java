package fh.server.rest.dto;

public class AccountDTO {

    private Long id;

    private String email;

    private String password;

    private String token;

    private String address;

    private String phoneNumber;

    private Integer clearanceLevel;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
