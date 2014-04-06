package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.CityDAO;
import uk.bris.esserver.repository.entities.City;

public class CityService {

	CityDAO cdao = new CityDAO();
	public List<City> getListOfCity(){
    	List<City> listOfCity = cdao.findAll(City.class);
    	return listOfCity;
	}
	
	public int addCity(City city){
    	return cdao.save(City.class, city);
	}
}
