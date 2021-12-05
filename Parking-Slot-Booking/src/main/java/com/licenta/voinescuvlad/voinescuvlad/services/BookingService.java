package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.BookingDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Booking findById(int id);

    Booking save(BookingDto bookingDto);

    boolean isOverlapping(BookingDto bookingDto,int parkingId);

    List<Booking> findBookingByTheParking(int id);

    List<Booking> findAllByUser(int id);

    Booking update(Booking booking);

    void delete(int id);


}
