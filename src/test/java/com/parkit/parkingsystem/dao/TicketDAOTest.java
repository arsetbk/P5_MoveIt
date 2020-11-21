package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    private ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
    private LocalDateTime inTime = LocalDateTime.now();
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();

        ticket = new Ticket();
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
        ticket.setVehicleRegNumber("Test");
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(inTime);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveTicket() {
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("Test");
        assertEquals("Test", savedTicket.getVehicleRegNumber());
    }

    @Test
    void getTickets() {
        ticketDAO.saveTicket(ticket);

        Ticket ticketTwo = new Ticket();
        ticketTwo.setVehicleRegNumber("Test");
        ticketTwo.setParkingSpot(parkingSpot);
        ticketTwo.setInTime(inTime);
        ticketDAO.saveTicket(ticketTwo);

        List<Ticket> tickets = ticketDAO.getTickets("Test");
        assertEquals(2, tickets.size());
    }

    @Test
    void updateTicket() {
        ticketDAO.saveTicket(ticket);
        LocalDateTime outTime = LocalDateTime.now();

        Ticket ticketToUpdate = ticketDAO.getTicket("Test");
        ticketToUpdate.setPrice(3.5);
        ticketToUpdate.setOutTime(outTime);
        ticketDAO.updateTicket(ticketToUpdate);
        Ticket ticketUpdated = ticketDAO.getTicket("Test");
        assertEquals(3.5, ticketUpdated.getPrice());
    }
}