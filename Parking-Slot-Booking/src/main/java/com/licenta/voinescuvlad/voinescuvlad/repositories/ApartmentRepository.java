package com.licenta.voinescuvlad.voinescuvlad.repositories;

import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment,Integer> {


    List<Apartment> findAllByStatus(String status);

    List<Apartment> findByUser(User user);

    List<Apartment> findAllByStatusLikeAndPpnLessThan(String status,double ppn);

    List<Apartment> findAllByCityContainsAndStatusLike(String city,String status);

    List<Apartment> findAllByCountreyContainsAndStatusLike(String country, String status);

    List<Apartment> findAllByUserAndStatus(User user,String string);

}
