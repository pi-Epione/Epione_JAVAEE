package fr.epione.services;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.rmi.CORBA.Util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import fr.epione.entity.Device;
import fr.epione.entity.Doctor;
import fr.epione.entity.Patient;
import fr.epione.entity.User;
import fr.epione.interfaces.IuserServiceLocal;
import fr.epione.interfaces.IuserServiceRemote;
import fr.epione.utils.Utils;

@Stateless
public class userService implements IuserServiceLocal, IuserServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;
	HttpServletRequest req;

	@Override
	public int addUser(Doctor user) {
		em.persist(user);
		return user.getId();
	}

	@Override
	public List<User> getAllUsers() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}

	@Override
	public User getUserById(int idUser) {
		return em.find(User.class, getIdUser(req));
	}

	@Override
	public List<User> getUsersEnable() {
		return em.createQuery("select u from User u where u.isEnable=1", User.class).getResultList();
	}

	@Override
	public Boolean isConnectedUser(int idUser) {
		try {
			return (Boolean) em.createQuery("select u.connected from User u where u.id = :idUser")
					.setParameter("idUser", idUser).getSingleResult();
		} catch (NoResultException e) {
			return false;
		}

	}

	@Override
	public JsonObject logIn(String email, String password, Device device) {

		try {
			password = Utils.toMD5(password);
			em.createQuery("SELECT u.id from User u WHERE u.email = :email and u.password = :password")
					.setParameter("email", email).setParameter("password", password).getSingleResult();
			getUserByEmail(email).setConnected(Boolean.TRUE);
			getUserByEmail(email).setLastConnect(new Date());

			device.setConnected(Boolean.TRUE);
			device.setLastConnection(new Date());
			device.setOwner(getUserByEmail(email));
			em.persist(device);

			return Json.createObjectBuilder().add("result", "Vous etes connecte")
					.add("id", getUserByEmail(email).getId()).build();
		} catch (NoResultException | NoSuchAlgorithmException e) {
			return Json.createObjectBuilder().add("result", "Verifier vos donnees").add("id", 0).build();
		}

	}

	@Override
	public JsonObject logOut(int idUser) {
		try {
			getUserById(idUser).setLastConnect(new Date());
			getUserById(idUser).setConnected(Boolean.FALSE);
			return Json.createObjectBuilder().add("success", "vous etes deconnecte").build();
		} catch (NoResultException e) {
			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
		}
	}

	@Override
	public JsonObject signInDoctor(Doctor doctor) {

		try {
			if (!Utils.emailValidator(doctor.getEmail()))
				return Json.createObjectBuilder().add("error", "Adresse email non valide !").build();

			if (emailExist(doctor.getEmail()) != 0)
				return Json.createObjectBuilder().add("error", "Email existe deja !").build();

			if (Utils.passwordValidator(doctor.getPassword()) != null) {
				JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
				int i = 1;
				for (String error : Utils.passwordValidator(doctor.getPassword())) {
					errorBuilder.add("error" + i, error);
					i++;
				}
				return errorBuilder.build();
			}
			doctor.setDateCreation(new Date());
			doctor.setIsEnable(Boolean.FALSE);
			doctor.setConnected(Boolean.FALSE);
			doctor.setPassword(Utils.toMD5(doctor.getPassword()));
			em.persist(doctor);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return Json.createObjectBuilder().add("success", "ajout effectue").add("id", doctor.getId()).build();

	}

	@Override
	public JsonObject signInPatient(Patient patient) {
		try {
			if (!Utils.emailValidator(patient.getEmail()))
				return Json.createObjectBuilder().add("error", "Adresse email non valide !").build();

			if (emailExist(patient.getEmail()) != 0)
				return Json.createObjectBuilder().add("error", "Email existe deja !").build();

			if (Utils.passwordValidator(patient.getPassword()) != null) {
				JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
				int i = 1;
				for (String error : Utils.passwordValidator(patient.getPassword())) {
					errorBuilder.add("error" + i, error);
					i++;
				}
				return errorBuilder.build();
			}
			patient.setDateCreation(new Date());
			patient.setIsEnable(Boolean.FALSE);
			patient.setConnected(Boolean.FALSE);
			patient.setPassword(Utils.toMD5(patient.getPassword()));
			em.persist(patient);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return Json.createObjectBuilder().add("success", "ajout effectue").add("id", patient.getId()).build();

	}

	/*
	 * @Override public JsonObject FirstSignUpDoctor(Doctor d) { return
	 * Json.createObjectBuilder().add("error",
	 * "your username or your token is not correct").build(); }
	 */

	@Override
	public User getUserByEmail(String email) {
		try {
			return (User) em.createQuery("select u from User u where u.email= :email").setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public long emailExist(String email) {
		return (long) em.createQuery("select count(u) from User u where u.email = :email").setParameter("email", email)
				.getSingleResult();
	}

	@Override
	public int getIdUser(HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
			int id = (int) session.getAttribute("user");
			return id;
		} catch (NullPointerException e) {
			return 0;
		}

	}

	@Override
	public boolean checkConnectedDevice(String host, String os, String browser, User user) {

		Long result = em
				.createQuery(
						"select count(d) from Device d where d.os =:os and d.ip = :host and d.browser = :browser and d.owner = :user",
						Long.class)
				.setParameter("os", os).setParameter("host", host).setParameter("browser", browser)
				.setParameter("user", user).getSingleResult();
		if (result != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean logOutFromDevice(int id) {
		User user = em.find(User.class, id);
		Device device = em.createQuery("select d from Device d where d.owner = :user", Device.class)
				.setParameter("user", user).getSingleResult();
		em.remove(device);
		return true;
	}

}
