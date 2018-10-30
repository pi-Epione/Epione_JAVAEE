package fr.epione.interfaces;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import fr.epione.entity.Adresse;
import fr.epione.entity.Calendrier;
import fr.epione.entity.Doctor;
import fr.epione.entity.Horaires;
import fr.epione.entity.Journee;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.User;

@Local
public interface IdoctorServiceLocal {

	JsonObject addMotif(MotifDoctor motif);

	JsonObject initialCalendar(int idDoctor);

	JsonObject addJournee(int id, Date date, HashMap<Integer, Integer> listHorairesPerso);
	
	JsonObject updateJournee(int idDoctor,Date date,HashMap<Integer, Integer> listHorairesPerso);

	MotifDoctor getMotifById(int idMotif);

	JsonObject initialMotifs(List<Integer> idsMotif, int idDoctor);

	JsonObject updateMotif(List<Integer> idsMotif, int idDoctor);

	List<MotifDoctor> getListMotifsByDoctor(int idDoctor);

	List<MotifDoctor> getAllMotifs();

	boolean deleteJournee(int id, Date date);
	
	

	List<Horaires> getHorairesJournees(int id, Date date);

	List<Doctor> getDoctorBySpeciality(String specialite);

	List<Doctor> getDoctorsDisponibleByDate(Date date);


	List<Doctor> getDoctorByLocation(Adresse adresse);

	boolean deleteHoraires(int idDoctor,Date date);
	
	Calendrier getCalendar(int idDoctor);
}
