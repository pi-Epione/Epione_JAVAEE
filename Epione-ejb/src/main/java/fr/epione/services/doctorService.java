package fr.epione.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.Doc;
import javax.ws.rs.core.Response;

import fr.epione.entity.Calendrier;
import fr.epione.entity.Doctor;
import fr.epione.entity.HorairesCalendar;
import fr.epione.entity.MotifDoctor;
import fr.epione.interfaces.IdoctorServiceLocal;
import fr.epione.interfaces.IdoctorServiceRemote;
import fr.epione.interfaces.IuserServiceLocal;

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
	public JsonObject initialCalendar() {

		HorairesCalendar horaires = new HorairesCalendar();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date currentDate = new Date();
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 8);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		horaires.setDate(c.getTime());
		horaires.setDisponible(Boolean.TRUE);
		em.persist(horaires);
		return Json.createObjectBuilder().add("success", "intialiser").build();
	}

	@Override
	public Calendrier getCalendarByDoctorId() {
		Doctor doctor = em.find(Doctor.class, 1);
		return (Calendrier) em.createQuery("select c from Calendar c where c.doctor = :idDoctor")
				.setParameter("idDoctor", doctor).getSingleResult();
	}

	@Override
	public MotifDoctor getMotifById(int idMotif) {
		return em.find(MotifDoctor.class, idMotif);
	}

	@Override
	public List<MotifDoctor> getAllMotifs() {
		return em.createQuery("select m from MotifDoctor m",MotifDoctor.class).getResultList();
		
	}

}
