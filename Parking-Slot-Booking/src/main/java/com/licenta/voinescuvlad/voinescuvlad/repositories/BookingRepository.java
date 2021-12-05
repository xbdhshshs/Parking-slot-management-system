package com.licenta.voinescuvlad.voinescuvlad.repositories;

import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

    List<Booking> findAllByUserId(int id);

    List<Booking> findAllByParkingId(int id);

}
