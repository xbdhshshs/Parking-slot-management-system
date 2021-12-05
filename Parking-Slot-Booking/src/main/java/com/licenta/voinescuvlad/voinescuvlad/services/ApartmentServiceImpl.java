package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ApartmentDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.repositories.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private ApartmentRepository apartmentRepository;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository theApartmentRepository) {
        apartmentRepository = theApartmentRepository;
    }


    @Autowired
    DtoMapping converter;

    @Override
    public List<Apartment> findAllApartmentsByStatus(String status) {
        return apartmentRepository.findAllByStatus(status);
    }

    @Override
    public Apartment findById(int theId) {
        Optional<Apartment> result = apartmentRepository.findById(theId);

        Apartment theApartment = null;

        if(result.isPresent()){
            theApartment = result.get();
        }
        else {
            throw new RuntimeException("Did not find apartment id - " + theId);
        }

        return theApartment;
    }

    @Override
    public List<Apartment> findByUser(User user) {
        return apartmentRepository.findByUser(user);
    }

    @Override
    public List<Apartment> findAllAccepted() {
        return apartmentRepository.findAllByStatus("accepted");
    }

    @Override
    public List<Apartment> findAllAcceptedByUser(User user) {
        return apartmentRepository.findAllByUserAndStatus(user,"accepted");
    }

    @Override
    public List<Apartment> findByMaxPrice(String price) {
        if(price=="")
            price="0";
        return apartmentRepository.findAllByStatusLikeAndPpnLessThan("accepted",Double.parseDouble(price));
    }




    //AICI
    @Override
    public Apartment save(ApartmentDto addApartment) {
        Apartment apartment = converter.getApartmentFromApartmentDto(addApartment);
        apartment.setStatus("pending");
        return apartmentRepository.saveAndFlush(apartment);
    }

    //AICI
    @Override
    public Apartment update(ApartmentDto addApartment) {
        Apartment apartment = converter.updateApartmentFromApartmentDto(addApartment);
        return apartmentRepository.saveAndFlush(apartment);
    }

    //different cuz admin side
    //AICI
    @Override
    public Apartment updateStatus(ApartmentDto apartmentDto) {
        Apartment apartment = converter.updateApartmentStatusFromApartmentDto(apartmentDto);
        return apartmentRepository.saveAndFlush(apartment);
    }



    @Override
    public void delete(int theId) {

        apartmentRepository.deleteById(theId);

    }

    @Override
    public void delete(Apartment apartment) {
        apartmentRepository.delete(apartment);
    }

    @Override
    public List<Apartment> findAllByTheCity(String city) {
        return apartmentRepository.findAllByCityContainsAndStatusLike(city,"accepted");
    }

    @Override
    public List<Apartment> findAllByTheCountry(String country) {
        return apartmentRepository.findAllByCountreyContainsAndStatusLike(country,"accepted");
    }


}
