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
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import javax.ws.rs.core.Response;

import fr.epione.entity.Adresse;
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
	public List<MotifDoctor> getAllMotifs() {
		return em.createQuery("select m from MotifDoctor m", MotifDoctor.class).getResultList();
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
	public List<MotifDoctor> getListMotifsByDoctor(int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		return doctor.getListMotifs();
	}

	@Override
	public MotifDoctor getMotifById(int idMotif) {
		return em.find(MotifDoctor.class, idMotif);
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
	public Calendrier getCalendar(int idDoctor) {
		Doctor doctor = em.find(Doctor.class, idDoctor);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor = :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();

		return calendrier;
	}
	
	@Override
	public List<Doctor> getDoctorBySpeciality(String specialite) {
		List<Doctor> listdoctors = em
				.createQuery("select d from Doctor d where d.specialite =:specialite", Doctor.class)
				.setParameter("specialite", specialite).getResultList();

		return listdoctors;
	}

	@Override
	public List<Doctor> getDoctorsDisponibleByDate(Date date) {
		Calendar dateFromUser = Calendar.getInstance();
		dateFromUser.setTime(date);
		List<Doctor> listDoctorsDisponible = new ArrayList<>();
		List<Doctor> listDoctors = em.createQuery("select d from Doctor d", Doctor.class).getResultList();
		for (Doctor doctor : listDoctors) {

			try {
				Calendrier calendrier = em
						.createQuery("select c from  Calendrier c where c.doctor = :doctor", Calendrier.class)
						.setParameter("doctor", doctor).getSingleResult();

				for (Journee journee : calendrier.getListJournees()) {
					boolean test = false;
					Calendar dateFromJournee = Calendar.getInstance();
					dateFromJournee.setTime(journee.getDate());

					if (dateFromUser.get(Calendar.YEAR) == dateFromJournee.get(Calendar.YEAR)
							&& dateFromUser.get(Calendar.MONTH) == dateFromJournee.get(Calendar.MONTH)
							&& dateFromUser.get(Calendar.DAY_OF_MONTH) == dateFromJournee.get(Calendar.DAY_OF_MONTH)) {

						List<Horaires> listHoraires = journee.getListHoraires();
						for (Horaires horaire : listHoraires) {
							if (horaire.getDisponible()) {
								listDoctorsDisponible.add(doctor);
							}
							test = true;
							break;
						}
					}
					if (test)
						break;
				}

			} catch (NoResultException e) {

			}

		}
		return listDoctorsDisponible;
	}

	@Override
	public List<Doctor> getDoctorByLocation(Adresse adresse) {
		List<Doctor> listDoctorsByAdresse = new ArrayList<>();
		List<Doctor> listDoctors = em.createQuery("select d from Doctor d", Doctor.class).getResultList();
		for (Doctor doctor : listDoctors) {
			if (doctor.getAdresse().getVille() == adresse.getVille()
					|| doctor.getAdresse().getRue() == adresse.getRue()) {
				listDoctorsByAdresse.add(doctor);
			}
		}
		return listDoctors;
	}

	

	@Override
	public JsonObject addJournee(int id, Date date, HashMap<Integer, Integer> listHorairesPerso) {
		Calendar dateFromUser = Calendar.getInstance();
		dateFromUser.setTime(date);
		Calendar dateFromJournee = Calendar.getInstance();
		Doctor doctor = em.find(Doctor.class, id);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor = :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();
		for (Journee journee : calendrier.getListJournees()) {
			dateFromJournee.setTime(journee.getDate());
			if (dateFromUser.get(Calendar.YEAR) == dateFromJournee.get(Calendar.YEAR)
					&& dateFromUser.get(Calendar.MONTH) == dateFromJournee.get(Calendar.MONTH)
					&& dateFromUser.get(Calendar.DAY_OF_MONTH) == dateFromJournee.get(Calendar.DAY_OF_MONTH)) {
				return Json.createObjectBuilder().add("error", "date invalide elle existe deja").build();
			}
		}

		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(new Date());
		Calendar newDate = Calendar.getInstance();
		HashMap<Integer, Integer> listHorairesValid = new HashMap<>();
		HashMap<Integer, Integer> listHorairesError = new HashMap<>();
		for (Map.Entry mapentry : listHorairesPerso.entrySet()) {
			if (Utils.validateTime((int) mapentry.getKey(), (int) mapentry.getValue(), date)) {
				listHorairesValid.put((int) mapentry.getKey(), (int) mapentry.getValue());
			} else {
				listHorairesError.put((int) mapentry.getKey(), (int) mapentry.getValue());
			}

		}

		calendrier.getListJournees().add(personnaliserJournee(date, listHorairesValid));
		em.persist(calendrier);

		return Json.createObjectBuilder().add("success", listHorairesValid.toString())
				.add("error", listHorairesError.toString()).build();

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
					if (horaires.getDisponible())
						em.remove(horaires);
					else
						return false;
				}
				em.remove(journee);
			} else {
				listJourneeNV.add(journee);
			}
		}
		calendrier.setListJournees(listJourneeNV);
		return true;
	}
	
	@Override
	public boolean deleteHoraires(int idDoctor, Date date) {
		Calendar dateFromUser = Calendar.getInstance();
		Doctor doctor = em.find(Doctor.class, idDoctor);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor = :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();
		List<Journee> listJournees = calendrier.getListJournees();
		for (Journee journee : listJournees) {
			Calendar c = Calendar.getInstance();
			c.setTime(journee.getDate());
			if (c.get(Calendar.YEAR) == dateFromUser.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == dateFromUser.get(Calendar.MONTH)
					&& c.get(Calendar.DAY_OF_MONTH) == dateFromUser.get(Calendar.DAY_OF_MONTH)) {
				List<Horaires> listHoraires = journee.getListHoraires();
				for (Horaires horaire : listHoraires) {
					c.setTime(horaire.getTime());
					if (c.get(Calendar.HOUR_OF_DAY) == dateFromUser.get(Calendar.HOUR_OF_DAY)
							&& c.get(Calendar.MINUTE) == dateFromUser.get(Calendar.MINUTE) && horaire.getDisponible()) {
						horaire.setJournee(null);
						em.remove(horaire);
						return true;
					}
				}
			}

		}
		return false;
	}

	@Override
	public List<Horaires> getHorairesJournees(int id, Date date) {
		Doctor doctor = em.find(Doctor.class, id);
		Calendrier calendrier = em.createQuery("select c from Calendrier c where c.doctor= :doctor", Calendrier.class)
				.setParameter("doctor", doctor).getSingleResult();
		List<Journee> listJournees = calendrier.getListJournees();
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		for (Journee journee : listJournees) {
			c2.setTime(journee.getDate());
			if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
					&& c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
				return journee.getListHoraires();

			}
		}
		return null;
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
			Calendar c = Calendar.getInstance();

			Horaires horaires = new Horaires();
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



	

	@Override
	public JsonObject updateJournee(int idDoctor, Date date, HashMap<Integer, Integer> listHorairesPerso) {
		/*Doctor doctor = em.find(Doctor.class, idDoctor);
		if(getJourneeByDate(doctor, date) != null){
			List<Horaires> listhoraires = getJourneeByDate(doctor, date).getListHoraires();
			for (Map.Entry mapentry : listHorairesPerso.entrySet()) {
				Calendar timeFromUser = Calendar.getInstance();
				timeFromUser.set(Calendar.HOUR_OF_DAY, (int)mapentry.getKey());
				timeFromUser.set(Calendar.MINUTE, (int)mapentry.getValue());
				for(Horaires horaire : listhoraires){
					Calendar timeFromHoraire = Calendar.getInstance();
					if(timeFromUser.get(Calendar.HOUR_OF_DAY) == timeFromHoraire.get(Calendar.HOUR_OF_DAY) 
							&& timeFromUser.get(Calendar.MINUTE) == timeFromHoraire.get(Calendar.MINUTE) && !horaire.getDisponible()){
						
					}
				}
			}
			
		}*/
		return null;
	}

	public Journee getJourneeByDate(Doctor doctor, Date date) {
		Calendar dateFromUser = Calendar.getInstance();
		dateFromUser.setTime(date);
		try {
			Calendrier calendrier = em
					.createQuery("select c from Calendrier c where c.doctor =:doctor", Calendrier.class)
					.setParameter("doctor", doctor).getSingleResult();
			for(Journee journee : calendrier.getListJournees()){
				Calendar dateFromJournee = Calendar.getInstance();
				dateFromJournee.setTime(journee.getDate());
				if (dateFromUser.get(Calendar.YEAR) == dateFromJournee.get(Calendar.YEAR)
						&& dateFromUser.get(Calendar.MONTH) == dateFromJournee.get(Calendar.MONTH)
						&& dateFromUser.get(Calendar.DAY_OF_MONTH) == dateFromJournee.get(Calendar.DAY_OF_MONTH)){
					return journee;
				}
			}
			
		} catch (NoResultException e) {
			return null;
		}

		return null;
	}

}
