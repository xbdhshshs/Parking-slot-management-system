package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.BookingDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import com.licenta.voinescuvlad.voinescuvlad.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DtoMapping converter;

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking save(BookingDto bookingDto) {
        Booking booking = converter.getBookingFromBookingDto(bookingDto);
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    public boolean isOverlapping(BookingDto bookingDto, int parkingId) {

        boolean isCommon =false;

        if (bookingDto.getCheckIn().before(bookingDto.getCheckOut())) {
            List<Booking> bookingsFromParking = bookingRepository.findAllByParkingId(parkingId);
            List<Date> myDates = calculateDaysBetween(bookingDto);
            List<Date> occupied = new ArrayList<>();
            for (Booking b : bookingsFromParking) {
                occupied.addAll(calculateDaysBetween(b));
            }
            isCommon = Collections.disjoint(myDates, occupied);
        }
        return isCommon;

    }

    private List<Date> calculateDaysBetween(Booking booking) {
        List<Date> unavailableDays = new ArrayList<>();
        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.setTime(booking.getCheckIn());

            Calendar toCal = Calendar.getInstance();
            toCal.setTime(booking.getCheckOut());

            while (!fromCal.after(toCal)) {
                unavailableDays.add(fromCal.getTime());
                fromCal.add(Calendar.DATE, 1);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return unavailableDays;
    }

    private List<Date> calculateDaysBetween(BookingDto booking) {
        List<Date> unavailableDays = new ArrayList<>();
        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.setTime(booking.getCheckIn());

            Calendar toCal = Calendar.getInstance();
            toCal.setTime(booking.getCheckOut());

            while (!fromCal.after(toCal)) {
                unavailableDays.add(fromCal.getTime());
                fromCal.add(Calendar.DATE, 1);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return unavailableDays;
    }



    @Override
    public List<Booking> findBookingByTheParking(int id) {
        return bookingRepository.findAllByParkingId(id);
    }

    @Override
    public List<Booking> findAllByUser(int id) {
        return bookingRepository.findAllByUserId(id);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    public void delete(int id) {
        bookingRepository.deleteById(id);
    }


}
