package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import fr.epione.entity.Calendrier;
import fr.epione.entity.HorairesCalendar;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.User;

@Local
public interface IdoctorServiceLocal {

	JsonObject addMotif(MotifDoctor motif);

	JsonObject initialCalendar();

	Calendrier getCalendarByDoctorId();

	MotifDoctor getMotifById(int idMotif);

	JsonObject initialMotifs(List<Integer> idsMotif,int idDoctor);

	JsonObject updateMotif(List<Integer> idsMotif,int idDoctor);

	List<MotifDoctor> getListMotifsByDoctor(int idDoctor);
	
	List<MotifDoctor> getAllMotifs();
	
	
}
