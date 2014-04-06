package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.PhotosDAO;
import uk.bris.esserver.repository.entities.Photos;

public class PhotosService {

	PhotosDAO pdao = new PhotosDAO();
	public List<Photos> getListOfPhotos(){
    	List<Photos> listOfPhotos = pdao.findAll(Photos.class);
    	return listOfPhotos;
	}
	
	public Photos getPhotoById(String id){
    	Photos photo = pdao.findOne(Photos.class, id);
    	return photo;
	}
	
	public int addPhoto(Photos photo){
    	return pdao.save(Photos.class, photo);
	}
	
	public void deletePhoto(String id){
    	pdao.delete(Photos.class, id);
	}
}
