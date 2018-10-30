package fr.epione.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.json.JsonObject;

import fr.epione.entity.Doctor;
import fr.epione.entity.Message;
import fr.epione.entity.Patient;

@Remote
public interface ImessageServiceRemote {
	
	public JsonObject sendMessage(Message message) ;  
	public List<Message> getMessagesByDoctor(int doctorId) ;
	public List<Message> getMessageByPatient(int patientId) ; 
	public List<Message> getTodayMessages() ;
	public boolean getSeenMessage(int messageId) ;
	public JsonObject deleteMessage(int messageId) ; 
	public Long countMessagesByPatient(int patientId) ;
	public Long countMessagesByDoctor(int doctorId) ;
	

}
