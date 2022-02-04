package com.newenergytrading.netplan.repository;

import com.newenergytrading.netplan.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String> {

   // public Users findByUsername(String username);

    public List<Users> findByUsername(String username);

  //  public Users findByUsernameAndPassword(String username, String password);

   // public String getPasswordByUsername(String username);
}
