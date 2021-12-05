package com.licenta.voinescuvlad.voinescuvlad.repositories;

import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserName(String userName);

}
