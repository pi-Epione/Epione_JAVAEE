package fr.epione.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import javax.ws.rs.core.Response;

import fr.epione.entity.Calendrier;
import fr.epione.entity.Doctor;
import fr.epione.entity.Horaires;
import fr.epione.entity.Journee;
import fr.epione.entity.MotifDoctor;
import fr.epione.interfaces.IdoctorServiceLocal;
import fr.epione.interfaces.IdoctorServiceRemote;
import fr.epione.interfaces.IuserServiceLocal;
import fr.epione.utils.Utils;

@Stateless
public class doctorService implements IdoctorServiceLocal, IdoctorServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;
	@EJB
	IuserServiceLocal userservice;

	@Override
	public JsonObject addMotif(MotifDoctor motif) {
		em.persist(motif);
		return Json.createObjectBuilder().add("success", motif.getId()).build();
	}

	@Override
	public List<MotifDoctor> getListMotifsByDoctor(int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		return doctor.getListMotifs();
	}

	@Override
	public JsonObject initialMotifs(List<Integer> idsMotif, int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		for (Integer idMotif : idsMotif) {
			MotifDoctor motif = em.find(MotifDoctor.class, idMotif);
			doctor.getListMotifs().add(motif);
		}
		return Json.createObjectBuilder().add("success", "motif initialise").build();
	}

	@Override
	public JsonObject updateMotif(List<Integer> idsMotif, int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		List<MotifDoctor> listMotifs = new ArrayList<>();

		for (Integer idMotif : idsMotif) {
			MotifDoctor motif = em.find(MotifDoctor.class, idMotif);
			listMotifs.add(motif);
		}
		doctor.setListMotifs(listMotifs);

		return Json.createObjectBuilder().add("success", "motif modifier").build();
	}

	@Override
	public MotifDoctor getMotifById(int idMotif) {
		return em.find(MotifDoctor.class, idMotif);
	}

	@Override
	public List<MotifDoctor> getAllMotifs() {
		return em.createQuery("select m from MotifDoctor m", MotifDoctor.class).getResultList();

	}

	@Override
	public JsonObject initialCalendar(int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		Calendrier calendrier = new Calendrier();

		List<Journee> listJournees = initialJournee();
		calendrier.setListJournees(listJournees);
		calendrier.setDoctor(doctor);
		em.persist(calendrier);

		return Json.createObjectBuilder().add("success", "intialiser").build();
	}

	@Override
	public JsonObject updateCalendar(int id, Date date, HashMap<Integer, Integer> listHorairesPerso) {
		Doctor doctor = em.find(Doctor.class, id);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor = :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();
		calendrier.getListJournees().add(personnaliserJournee(date, listHorairesPerso));
		em.persist(calendrier);

		return Json.createObjectBuilder().add("success", "calendrier modifier").build();

	}

	@Override
	public boolean deleteJournee(int id, Date date) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date);
		Doctor doctor = em.find(Doctor.class, id);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor = :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();
		List<Journee> listJournee = calendrier.getListJournees();
		List<Journee> listJourneeNV = new ArrayList<>();
		for (Journee journee : listJournee) {
			c1.setTime(journee.getDate());

			if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
					&& c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {

				List<Horaires> listHoraires = em
						.createQuery("select h from Horaires h where h.journee = :journee", Horaires.class)
						.setParameter("journee", journee).getResultList();
				for (Horaires horaires : listHoraires) {
					em.remove(horaires);
				}

				em.remove(journee);
			} else {
				listJourneeNV.add(journee);
			}

		}

		calendrier.setListJournees(listJourneeNV);
		return true;
	}

	/*****
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * **/

	public Journee personnaliserJournee(Date date, HashMap<Integer, Integer> listHorairesPerso) {

		Journee journee = new Journee();

		journee.setDate(date);

		List<Horaires> listHoraires = personnaliserHoraires(listHorairesPerso, journee);
		journee.setListHoraires(listHoraires);
		em.persist(journee);
		return journee;
	}

	public List<Horaires> personnaliserHoraires(HashMap<Integer, Integer> listHorairesPerso, Journee journee) {

		List<Horaires> listHoraires = new ArrayList<>();
		for (Map.Entry mapentry : listHorairesPerso.entrySet()) {

			Horaires horaires = new Horaires();
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, (int) mapentry.getKey());
			c.set(Calendar.MINUTE, (int) mapentry.getValue());
			horaires.setTime(c.getTime());
			horaires.setDisponible(Boolean.TRUE);
			horaires.setJournee(journee);
			em.persist(horaires);
			listHoraires.add(horaires);
		}
		return listHoraires;
	}

	public List<Journee> initialJournee() {
		List<Journee> listJournees = new ArrayList<>();
		Journee journee = new Journee();
		Date date = new Date();
		journee.setDate(date);

		List<Horaires> listHoraires = intialHoraires(journee);
		journee.setListHoraires(listHoraires);
		listJournees.add(journee);
		em.persist(journee);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		for (int i = 0; i < 5; i++) {
			Journee journee2 = new Journee();

			c.add(Calendar.DAY_OF_MONTH, 1);

			journee2.setDate(c.getTime());
			listHoraires = intialHoraires(journee2);
			journee2.setListHoraires(listHoraires);
			listJournees.add(journee2);
			em.persist(journee2);
		}

		return listJournees;

	}

	public List<Horaires> intialHoraires(Journee journee) {

		Horaires horaires = new Horaires();
		List<Horaires> listHoraires = new ArrayList<>();
		int hours = 8;
		int minute = 0;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hours);
		c.set(Calendar.MINUTE, minute);

		horaires.setTime(c.getTime());
		horaires.setDisponible(Boolean.TRUE);
		horaires.setJournee(journee);
		em.persist(horaires);
		listHoraires.add(horaires);
		for (int i = 0; i < 11; i++) {
			c.add(Calendar.MINUTE, 20);
			Horaires horaires2 = new Horaires();
			horaires2.setTime(c.getTime());
			horaires2.setDisponible(Boolean.TRUE);
			horaires2.setJournee(journee);
			em.persist(horaires2);
			listHoraires.add(horaires2);
		}
		hours = 13;
		minute = 40;
		c.set(Calendar.HOUR_OF_DAY, hours);
		c.set(Calendar.MINUTE, minute);
		for (int i = 0; i < 10; i++) {
			c.add(Calendar.MINUTE, 20);
			Horaires horaires2 = new Horaires();
			horaires2.setTime(c.getTime());
			horaires2.setDisponible(Boolean.TRUE);
			horaires2.setJournee(journee);
			em.persist(horaires2);
			listHoraires.add(horaires2);
		}
		return listHoraires;
	}

}
