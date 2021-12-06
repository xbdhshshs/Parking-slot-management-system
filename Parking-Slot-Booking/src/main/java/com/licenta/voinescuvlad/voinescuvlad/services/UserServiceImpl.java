package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.UserRegistrationDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Role;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    DtoMapping converter;


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new RuntimeException("Did not find user id - " + id);
        }

        return theUser;
    }


    @Override
    public User save(UserRegistrationDto registration) {
        User user = converter.getUserFromUserDto(registration);
        return userRepository.saveAndFlush(user);

    }

    @Override
    public void update(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public int max(){
        return userRepository.max();
    }
}
