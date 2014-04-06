package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.EventDAO;
import uk.bris.esserver.repository.entities.Event;

public class EventService {

	EventDAO edao = new EventDAO();
	
	public List<Event> getListOfEvent(){
		List<Event> listOfEvent = edao.findAll(Event.class);
		return listOfEvent;
	}

	public Event getEventById(String id){
		Event event = edao.findOne(Event.class, id);
		return event;
	}

	public List<Event> getEventByCity(String city){
		List<Event> events = edao.findByCity(Event.class, city.toUpperCase());
		return events;
	}

	public List<Event> getEventByUserId(String userid){
		List<Event> events = edao.findByUserId(Event.class, userid);
		return events;
	}

	public int addEvent(Event event){
		return edao.save(Event.class, event);
	}
	
	public int updateEvent(Event event){
		return edao.saveOrUpdate(Event.class, event);
	}
}
