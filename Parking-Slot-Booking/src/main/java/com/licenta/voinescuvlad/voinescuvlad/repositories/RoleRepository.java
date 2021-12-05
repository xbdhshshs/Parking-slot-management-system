package com.licenta.voinescuvlad.voinescuvlad.repositories;

import com.licenta.voinescuvlad.voinescuvlad.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {

}