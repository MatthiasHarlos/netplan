package com.newenergytrading.netplan.domain;

import com.newenergytrading.netplan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void registerNewUserAccount(Users user)  {
        repository.save(user);
    }

}
