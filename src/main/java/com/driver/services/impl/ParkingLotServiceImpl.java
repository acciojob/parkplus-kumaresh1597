package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        Spot newSpot = new Spot();
        // choosing Spottype as per wheels
        if(numberOfWheels <= 2){
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels <=4) {
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            newSpot.setSpotType(SpotType.OTHERS);
        }
        //set the attributes in new spot
        newSpot.setPricePerHour(pricePerHour);
        newSpot.setOccupied(false);
        // creating the foreign key
        newSpot.setParkingLot(parkingLot);
        // adding the spot to parkinglot
        parkingLot.getSpotList().add(newSpot);
        // saving in the parent repository
        parkingLotRepository1.save(parkingLot);

        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();

        ParkingLot parkingLot = spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);

    //    spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        spotRepository1.save(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot s : spotList){
            spotRepository1.deleteById(s.getId());
        }
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
