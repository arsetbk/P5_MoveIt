package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();

        parkingSpot = new ParkingSpot(1, CAR,false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNextAvailableSlot() {
        ParkingType type = CAR;
        int parkSpot = parkingSpotDAO.getNextAvailableSlot(type);
        assertEquals(1, parkSpot);
    }

    @Test
    void updateParking() {
        boolean spotUpdated = parkingSpotDAO.updateParking(parkingSpot);
        assertEquals(true, spotUpdated);
    }
}