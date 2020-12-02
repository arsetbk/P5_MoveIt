package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class DAO processing the interactions of the ParkingSpot Object with
 * the Database
 */
public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * @param ticket a Ticket Object that is going to be save in DB
     * @return a boolean describing the save execution state
     */
    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            try (PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET)) {
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ps.setInt(1,ticket.getId());
                ps.setInt(1,ticket.getParkingSpot().getId());
                ps.setString(2, ticket.getVehicleRegNumber());
                ps.setDouble(3, ticket.getPrice());
                ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
                ps.setTimestamp(5, (ticket.getOutTime() == null)?null: Timestamp.valueOf(ticket.getOutTime()) );
                return ps.execute();
            }catch (Exception ex){
                logger.error("Error fetching next available slot",ex);
            }
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
                dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * @param vehicleRegNumber a String representing the user vehicle
     * @return a Ticket Object with the RegNumber searched
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
                Timestamp outTime = rs.getTimestamp(5);
                if(outTime != null)
                    ticket.setOutTime(rs.getTimestamp(5).toLocalDateTime());
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }

    /**
     * @param vehicleRegNumber a String representing the user vehicle
     * @return a List of Ticket Object representing the history of the user
     */
    public List<Ticket> getTickets(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket;
        List<Ticket> tickets = new ArrayList<Ticket>();
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKETS);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ticket = new Ticket();
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                tickets.add(ticket);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return tickets;
    }

    /**
     * @param ticket a Ticket Object that is the ticket to update
     * @return a boolean describing the state of the update
     */
    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            try(PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET)){
                ps.setDouble(1, ticket.getPrice());
                ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
                ps.setInt(3,ticket.getId());
                ps.execute();
                return true;
            } catch (Exception ex){
                logger.error("Error saving ticket info",ex);
            }
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }
}
