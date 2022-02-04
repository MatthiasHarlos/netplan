package com.newenergytrading.netplan.domain;

import com.newenergytrading.netplan.forms.KnotInputForm;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "project_name")
    private String projectname;
    @OneToMany (mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KnotInputForm> knotinputform;
    @ManyToOne
    @JoinColumn(name = "username")
    private Users username;

    public Project(String projectname, Users username) {
        this.projectname = projectname;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String project_name) {
        this.projectname = project_name;
    }

    public List<KnotInputForm> getKnotinputform() {
        return knotinputform;
    }

    public void setKnotinputform(List<KnotInputForm> knotinputform) {
        this.knotinputform = knotinputform;
    }

    public Users getUsername() {
        return username;
    }

    public void setUsername(Users username) {
        this.username = username;
    }

    public Project() {
    }
}
