package com.newenergytrading.netplan.repository;

import com.newenergytrading.netplan.domain.Project;
import com.newenergytrading.netplan.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

        public List<Project> findByProjectname(String project_name);

        //public Project findByProjectnameAndUser(String project_name, Users username);

        //public void deleteByProjectname(String project_name);

        public void deleteByProjectnameAndUsername(String project_name, Users username);

        public List<Project> findByUsername(Users username);
}
