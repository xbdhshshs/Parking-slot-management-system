package com.licenta.voinescuvlad.voinescuvlad.repositories;

import com.licenta.voinescuvlad.voinescuvlad.entities.Parking;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking,Integer> {


    List<Parking> findAllByStatus(String status);

    List<Parking> findByUser(User user);

    List<Parking> findAllByStatusLikeAndPpnLessThan(String status, double ppn);

    List<Parking> findAllByCityContainsAndStatusLike(String city, String status);

    List<Parking> findAllByCountreyContainsAndStatusLike(String country, String status);

    List<Parking> findAllByUserAndStatus(User user, String string);

    @Query(value = "SELECT max(id) FROM Parking")
    public int max();

}
