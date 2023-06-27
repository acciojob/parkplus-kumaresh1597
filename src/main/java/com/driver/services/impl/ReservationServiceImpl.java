package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> optionalUser = userRepository3.findById(userId);
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository3.findById(parkingLotId);
        if(!optionalUser.isPresent() || !optionalParkingLot.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        User user = optionalUser.get();
        ParkingLot parkingLot = optionalParkingLot.get();

        Spot spot = null;
        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot sp : spotList){
            if(numberOfWheels <= 2 && sp.getSpotType().equals(SpotType.TWO_WHEELER) && !sp.getOccupied()){
                sp.setOccupied(true);
                spot = sp;
                break;
            } else if (numberOfWheels <= 4 && sp.getSpotType().equals(SpotType.FOUR_WHEELER) && !sp.getOccupied()) {
                sp.setOccupied(true);
                spot = sp;
                break;
            } else if(!sp.getOccupied()){
                spot = sp;
            }
        }
        if(spot == null){
            throw new Exception("Cannot make reservation");
        }
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);

        reservation.setUser(user);
        reservation.setSpot(spot);

        reservationRepository3.save(reservation);

        spot.getReservationList().add(reservation);
        spotRepository3.save(spot);

        user.getReservationList().add(reservation);
        userRepository3.save(user);

        return reservation;
    }
}
