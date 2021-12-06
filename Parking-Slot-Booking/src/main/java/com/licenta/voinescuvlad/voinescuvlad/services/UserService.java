package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.UserRegistrationDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    List<User> findAllUsers();

    User findById(int id);

    User save(UserRegistrationDto registration);

    void update(User user);

    void deleteUserById(int id);

    public int max();
}
