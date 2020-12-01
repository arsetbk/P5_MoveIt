package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){

        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        LocalDateTime inTime = ticket.getInTime();
        LocalDateTime outTime = ticket.getOutTime();

        //difference between the two timestamps then convert in hours
        Duration d = Duration.between(inTime, outTime);
        double duration = (double) d.toMinutes()/60;
        //double duration = (outTime - inTime) / (1000*60*60);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                double price = (duration < 0.5) ? 0: (duration - 0.5) * Fare.CAR_RATE_PER_HOUR;
                ticket.setPrice(price);
                break;
            }
            case BIKE: {
                double price = (duration < 0.5) ? 0: (duration - 0.5) * Fare.BIKE_RATE_PER_HOUR;
                ticket.setPrice(price);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}