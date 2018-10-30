package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Remote;
import javax.json.JsonObject;

import fr.epione.entity.Doctor;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;

@Remote
public interface IrendezvousServiceRemote {
	
	public JsonObject addRendezVous(RendezVous rdv) ; 
	public int updateRendezVous(RendezVous rdv) ; 
	public JsonObject cancelRendezVous(int rdvId) ; 
	//public JsonObject addMotifDoctor(MotifDoctor motif, int rendezVousId) ; 
	public List<RendezVous> getListRendezVous() ; 
	public List<RendezVous> getListRendezVousByMotif(int motifId) ; 
	public List<RendezVous> getListRendezVousByDoctor(int doctorId) ; 
	public List<RendezVous> getListRendezVousByPatient(int patientId) ; 
	public int confimRendezVous(int rendezvousId) ;
	public List<RendezVous> getStateRendezVous(int doctorId) ;
	public void send(String address, String topic, String textMessage); 
//	public void sendSMS(String numero, String msg) ; 
	

}
