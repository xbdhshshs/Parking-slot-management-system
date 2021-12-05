package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ApartmentDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {

    List<Apartment> findAllApartmentsByStatus(String status);

    Apartment findById(int theId);

    List<Apartment> findByUser(User user);

    List<Apartment> findAllAccepted();

    List<Apartment> findAllAcceptedByUser(User user);

    List<Apartment> findByMaxPrice(String price);

    Apartment update(ApartmentDto apartmentDto);

    //AICI
    Apartment save(ApartmentDto theApartment);

    Apartment updateStatus(ApartmentDto apartmentDto);

    void delete(int theId);

    void delete(Apartment apartment);

    List<Apartment> findAllByTheCity(String city);

    List<Apartment> findAllByTheCountry(String country);


}
