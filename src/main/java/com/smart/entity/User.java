package com.smart.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Name field is Required")
    @Size(min = 2 ,max=20,message = "min 2 and max 20 names are allowed !! ")
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String imgurl;
    @Column(length = 500)
    private String about;
    private boolean enabled;
    private String role;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user" ,orphanRemoval = true)
    private List<Contact> contact;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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


    public String getImgurl() {
        return imgurl;
    }


    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    public String getAbout() {
        return about;
    }


    public void setAbout(String about) {
        this.about = about;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public List<Contact> getContact() {
        return contact;
    }


    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }


    public User() {
        super();
    }


    public User(int id, String name, String email, String password, String imgurl, String about, boolean enabled,
            String role, List<Contact> contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imgurl = imgurl;
        this.about = about;
        this.enabled = enabled;
        this.role = role;
        this.contact = contact;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", imgurl="
                + imgurl + ", about=" + about + ", enabled=" + enabled + ", role=" + role + ", contact=" + contact
                + "]";
    }


  
    
}
