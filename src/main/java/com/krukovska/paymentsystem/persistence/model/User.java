package com.krukovska.paymentsystem.persistence.model;

public class User extends Entity {

    private String email;
    private String name;
    private String password;
    private Boolean isAdmin;
    private Client client;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "User: {email = '" + email + ", name = '" + name +
                ", password = '" + password + ", isAdmin = " + isAdmin +
                ", client = " + client + '}';
    }
}
