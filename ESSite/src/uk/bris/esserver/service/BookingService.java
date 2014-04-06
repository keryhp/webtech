package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.BookingDAO;
import uk.bris.esserver.repository.entities.Booking;

public class BookingService {

	BookingDAO bdao = new BookingDAO();
	public List<Booking> getListOfBooking(){
		List<Booking> listOfBooking = bdao.findAll(Booking.class);
		return listOfBooking;
	}

	public Booking getBookingById(String id){
		Booking comment = bdao.findOne(Booking.class, id);
		return comment;
	}

	public List<Booking> getBookingByEventId(String eventid){
		List<Booking> bookings = bdao.findByEventId(Booking.class, eventid);
		return bookings;
	}

	public List<Booking> getBookingByUserId(String userid){
		List<Booking> bookings = bdao.findByUserId(Booking.class, userid);
		return bookings;
	}

	public int addBooking(Booking comment){
		return bdao.save(Booking.class, comment);
	}
}
