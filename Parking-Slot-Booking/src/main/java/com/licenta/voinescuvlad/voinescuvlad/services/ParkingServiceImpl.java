package com.licenta.voinescuvlad.voinescuvlad.services;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ParkingDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Parking;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.repositories.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {

    private ParkingRepository parkingRepository;

    @Autowired
    public ParkingServiceImpl(ParkingRepository theParkingRepository) {
        parkingRepository = theParkingRepository;
    }


    @Autowired
    DtoMapping converter;

    @Override
    public List<Parking> findAllParkingsByStatus(String status) {
        return parkingRepository.findAllByStatus(status);
    }

    @Override
    public Parking findById(int theId) {
        Optional<Parking> result = parkingRepository.findById(theId);

        Parking theParking = null;

        if(result.isPresent()){
            theParking = result.get();
        }
        else {
            throw new RuntimeException("Did not find parking id - " + theId);
        }

        return theParking;
    }

    @Override
    public List<Parking> findByUser(User user) {
        return parkingRepository.findByUser(user);
    }

    @Override
    public List<Parking> findAllAccepted() {
        return parkingRepository.findAllByStatus("accepted");
    }

    @Override
    public List<Parking> findAllAcceptedByUser(User user) {
        return parkingRepository.findAllByUserAndStatus(user,"accepted");
    }

    @Override
    public List<Parking> findByMaxPrice(String price) {
        if(price=="")
            price="0";
        return parkingRepository.findAllByStatusLikeAndPpnLessThan("accepted",Double.parseDouble(price));
    }




    //AICI
    @Override
    public Parking save(ParkingDto addParking) {
        Parking parking = converter.getParkingFromParkingDto(addParking);
        parking.setStatus("pending");
        return parkingRepository.saveAndFlush(parking);
    }

    //AICI
    @Override
    public Parking update(ParkingDto addParking) {
        Parking parking = converter.updateParkingFromParkingDto(addParking);
        return parkingRepository.saveAndFlush(parking);
    }

    //different cuz admin side
    //AICI
    @Override
    public Parking updateStatus(ParkingDto parkingDto) {
        Parking parking = converter.updateParkingStatusFromParkingDto(parkingDto);
        return parkingRepository.saveAndFlush(parking);
    }



    @Override
    public void delete(int theId) {

        parkingRepository.deleteById(theId);

    }

    @Override
    public void delete(Parking parking) {
        parkingRepository.delete(parking);
    }

    @Override
    public List<Parking> findAllByTheCity(String city) {
        return parkingRepository.findAllByCityContainsAndStatusLike(city,"accepted");
    }

    @Override
    public List<Parking> findAllByTheCountry(String country) {
        return parkingRepository.findAllByCountreyContainsAndStatusLike(country,"accepted");
    }


}
