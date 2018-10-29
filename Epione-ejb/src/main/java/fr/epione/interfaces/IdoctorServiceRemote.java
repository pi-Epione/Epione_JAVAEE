
package fr.epione.interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;
import javax.json.JsonObject;

import fr.epione.entity.Adresse;
import fr.epione.entity.Calendrier;
import fr.epione.entity.Doctor;
import fr.epione.entity.Horaires;
import fr.epione.entity.Journee;
import fr.epione.entity.MotifDoctor;

@Remote
public interface IdoctorServiceRemote {
	JsonObject addMotif(MotifDoctor motif);

	JsonObject initialCalendar(int idDoctor);

	JsonObject addJournee(int id, Date date, HashMap<Integer, Integer> listHorairesPerso);

	MotifDoctor getMotifById(int idMotif);

	JsonObject initialMotifs(List<Integer> idsMotif, int idDoctor);

	JsonObject updateMotif(List<Integer> idsMotif, int idDoctor);

	List<MotifDoctor> getListMotifsByDoctor(int idDoctor);

	List<MotifDoctor> getAllMotifs();

	boolean deleteJournee(int id, Date date);

	List<Horaires> getHorairesJournees(int id, Date date);

	List<Doctor> getDoctorBySpeciality(String specialite);

	List<Doctor> getDoctorsDisponible();

	List<Doctor> getDoctorsBySpeciliteDisponible();

	List<Doctor> getDoctorByLocation(Adresse adresse);
	
	boolean deleteHoraires(int idDoctor,Date date);
	
	Calendrier getCalendar(int idDoctor);
}