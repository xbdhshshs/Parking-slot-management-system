package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.BookingDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Booking findById(int id);

    Booking save(BookingDto bookingDto);

    boolean isOverlapping(BookingDto bookingDto,int apartmentId);

    List<Booking> findBookingByTheApartment(int id);

    List<Booking> findAllByUser(int id);

    Booking update(Booking booking);

    void delete(int id);


}
