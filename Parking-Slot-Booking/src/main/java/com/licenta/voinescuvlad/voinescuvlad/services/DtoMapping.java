package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ApartmentDto;
import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.BookingDto;
import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.UserRegistrationDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import com.licenta.voinescuvlad.voinescuvlad.entities.Role;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DtoMapping {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    //AICI
    public Apartment getApartmentFromApartmentDto(ApartmentDto apartmentDto)
    {
        Apartment apartment = new Apartment();
        apartment.setId(apartmentDto.getApartmentID());
        apartment.setApartmentName(apartmentDto.getApartmentName());
        apartment.setCountrey(apartmentDto.getCountrey());
        apartment.setAdress(apartmentDto.getAdress());
        apartment.setCity(apartmentDto.getCity());
        apartment.setStatus(apartmentDto.getStatus());
        apartment.setApartmentImage(null);
        apartment.setPpn(apartmentDto.getPpn());
        apartment.setService(apartmentDto.getService());
        apartment.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return apartment;
    }

    //AICI
    public ApartmentDto getApartmentDtoFromApartment(Apartment apartment)
    {
        ApartmentDto apartmentDto = new ApartmentDto();
        apartmentDto.setApartmentID(apartment.getId());
        apartmentDto.setApartmentName(apartment.getApartmentName());
        apartmentDto.setCountrey(apartment.getCountrey());
        apartmentDto.setCity(apartment.getCity());
        apartmentDto.setAdress(apartment.getAdress());
        apartmentDto.setPpn(apartment.getPpn());
        apartmentDto.setService(apartment.getService());
        apartmentDto.setStatus(apartment.getStatus());
        apartmentDto.setUserName(apartment.getUser().getUserName());

        return  apartmentDto;
    }

    //AICI
    public Apartment updateApartmentFromApartmentDto(ApartmentDto apartmentDto)
    {
        Apartment apartment = apartmentService.findById(apartmentDto.getApartmentID());
        apartment.setApartmentName(apartmentDto.getApartmentName());
        apartment.setPpn(apartmentDto.getPpn());
        return apartment;
    }



    public Apartment updateApartmentStatusFromApartmentDto(ApartmentDto apartmentDto)
    {
        Apartment apartment = apartmentService.findById(apartmentDto.getApartmentID());
        apartment.setStatus(apartmentDto.getStatus());
        return apartment;
    }

    public User getUserFromUserDto(UserRegistrationDto registration)
    {
        User user = new User();
        user.setId(registration.getId());
        user.setUserName(registration.getUserName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setApartments(null);
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setPhone(registration.getPhone());
        user.setCarReg(registration.getCarReg());
        return user;
    }

    public UserRegistrationDto getUserDtoFromUser(User user)
    {
        UserRegistrationDto dto = new UserRegistrationDto();

        dto.setId(user.getId());

        dto.setUserName(user.getUserName());

        dto.setEmail(user.getEmail());

        dto.setPhone(user.getPhone());

        dto.setCarReg(user.getCarReg());

        dto.setPassword(user.getPassword());

        return dto;
    }

    public Booking getBookingFromBookingDto(BookingDto bookingDto)
    {
        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());
        booking.setCheckIn(bookingDto.getCheckIn());
        booking.setCheckOut(bookingDto.getCheckOut());
        booking.setApartment(getApartmentFromApartmentDto(bookingDto.getApartmentDto()));
        booking.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return booking;

    }

    public BookingDto getBookingDtoFromBooking(Booking booking)
    {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(booking.getBookingId());
        bookingDto.setCheckIn(booking.getCheckIn());
        bookingDto.setCheckOut(booking.getCheckOut());
        bookingDto.setApartmentDto(getApartmentDtoFromApartment(booking.getApartment()));

        return bookingDto;
    }


}
