package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import fr.epione.entity.Device;
import fr.epione.entity.Doctor;
import fr.epione.entity.Patient;
import fr.epione.entity.User;

@Local
public interface IuserServiceLocal {
	int addUser(Doctor user);
	List<User> getAllUsers();
	User getUserById(int id);
	User getUserByEmail(String email);
	List<User> getUsersEnable();
	Boolean isConnectedUser(int idUser);
	JsonObject logIn(String email,String password,Device device);
	JsonObject logOut(HttpServletRequest req,int idUser);
	JsonObject signInDoctor(Doctor doctor);
	JsonObject signInPatient(Patient patient);
	int getIdUser(HttpServletRequest req);
	boolean checkConnectedDevice(String host,String os,String browser,User user);
	boolean logOutFromDevice(HttpServletRequest req ,int id);
	boolean deleteDoctorById(int id);
	
	
	
}
