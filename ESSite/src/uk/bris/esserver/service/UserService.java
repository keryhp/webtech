package uk.bris.esserver.service;

import java.util.List;

import uk.bris.esserver.repository.dao.UserDAO;
import uk.bris.esserver.repository.entities.Users;

public class UserService {

	UserDAO udao = new UserDAO();
	public List<Users> getListOfUser(){
		List<Users> listOfUser = udao.findAll(Users.class);
		return listOfUser;
	}

	public Users getUserById(String id){
		Users user = udao.findOne(Users.class, id);
		return user;
	}

	public Users getUserByEmail(String email){
		Users user = udao.findByEmail(Users.class, email);
		return user;
	}

	public int addUser(Users user){
		return udao.save(Users.class, user);
	}
}
