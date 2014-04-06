package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.PeopleDAO;
import uk.bris.esserver.repository.entities.People;

public class PeopleService {

	PeopleDAO pdao = new PeopleDAO();
	public List<People> getListOfPeople(){
    	List<People> listOfPeople = pdao.findAll(People.class);
    	return listOfPeople;
	}
	
	public int addPeople(People ppl){
    	return pdao.save(People.class, ppl);
	}
}
