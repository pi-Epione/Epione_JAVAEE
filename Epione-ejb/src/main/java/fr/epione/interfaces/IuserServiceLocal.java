package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import fr.epione.entity.Doctor;
import fr.epione.entity.User;

@Local
public interface IuserServiceLocal {
	int addUser(Doctor user);
	List<User> getAllUsers();
	User getUserById(int idUser);
	User getUserByEmail(String email);
	List<User> getUsersEnable();
	Boolean isConnectedUser(int idUser);
	JsonObject logIn(String email,String password);
	JsonObject logOut(int idUser);
	JsonObject signIn(User user);
}
