package com.newenergytrading.netplan.domain;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    @Id
    private String username;
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @OneToMany(mappedBy = "username", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> project;

    public Users() {
        super();
        this.enabled=false;
    }

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //Configuration config = new Configuration();
        this.password = password;//config.passwordEncoder().encode(password);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
