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

import fr.epione.entity.Doctor;
import fr.epione.entity.User;
import fr.epione.interfaces.IuserServiceLocal;
import fr.epione.interfaces.IuserServiceRemote;
import fr.epione.utils.Utils;

@Stateless
public class userService implements IuserServiceLocal, IuserServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;

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
		return em.find(User.class, idUser);
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
	public JsonObject logIn(String email, String password) {
		try {
			password = Utils.toMD5(password);

			em.createQuery("SELECT u.id from User u WHERE u.email = :email and u.password = :password")
					.setParameter("email", email).setParameter("password", password).getSingleResult();
			getUserByEmail(email).setConnected(Boolean.TRUE);
			getUserByEmail(email).setLastConnect(new Date());
			return Json.createObjectBuilder().add("success", "Vous etes connecte")
					.add("id", getUserByEmail(email).getId()).build();
		} catch (NoResultException | NoSuchAlgorithmException e) {
			return Json.createObjectBuilder().add("error", "Verifier vos donnees").build();
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
	public JsonObject signIn(User user) {
		try {
			if (!Utils.emailValidator(user.getEmail()))
				return Json.createObjectBuilder().add("error", "Adresse email non valide !").build();

			if (emailExist(user.getEmail()) != 0)
				return Json.createObjectBuilder().add("error", "Email existe deja !").build();

			if (Utils.passwordValidator(user.getPassword()) != null) {
				JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
				int i = 1;
				for (String error : Utils.passwordValidator(user.getPassword())) {
					errorBuilder.add("error" + i, error);
					i++;
				}
				return errorBuilder.build();
			}
			user.setDateCreation(new Date());
			user.setIsEnable(Boolean.FALSE);
			user.setConnected(Boolean.FALSE);
			user.setPassword(Utils.toMD5(user.getPassword()));
			em.persist(user);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return Json.createObjectBuilder().add("success", "ajout effectue").add("id", user.getId()).build();
	}

	/*
	 * @Override public JsonObject FirstSignUpDoctor(Doctor d) { return
	 * Json.createObjectBuilder().add("error",
	 * "your username or your token is not correct").build(); }
	 */

	@Override
	public User getUserByEmail(String email) {
		return (User) em.createQuery("select u from User u where u.email= :email").setParameter("email", email)
				.getSingleResult();

	}

	public long emailExist(String email) {
		return (long) em.createQuery("select count(u) from User u where u.email = :email").setParameter("email", email)
				.getSingleResult();
	}

}
