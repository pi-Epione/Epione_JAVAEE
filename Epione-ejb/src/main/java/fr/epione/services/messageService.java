package fr.epione.services;

import java.time.LocalDate;



import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.http.impl.execchain.MainClientExec;


import fr.epione.entity.Doctor;
import fr.epione.entity.Message;
import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;
import fr.epione.interfaces.ImessageServiceLocal;
import fr.epione.interfaces.ImessageServiceRemote;

@Stateless
public class messageService implements ImessageServiceLocal,ImessageServiceRemote{

	
	@PersistenceContext(unitName = "epione")
	EntityManager em ; 
	
	/************************ done ************************/
	@Override
	public JsonObject sendMessage(Message message) {
		try{
		if(message.getDoctor()!=null && message.getPatient()!=null && message.getContent()!=null){
		em.persist(message);
		message.setDate(new Date());

		return Json.createObjectBuilder().add("success", "envoi message").build();
		}
		else  return Json.createObjectBuilder().add("error", "precise the content, sender and recipient").build();
		}
		catch (Exception e) {
			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
		}
	}

	/************************ done ************************/
	@Override
	public List<Message> getMessagesByDoctor(int doctorId) {
		Doctor doctor = em.find(Doctor.class, doctorId) ; 
		for(Message m : doctor.getMessages()){
			m.setSeen(true);
		}
		TypedQuery<Message> query = em.createQuery(
				"select m from Message m where m.doctor= :d  ", Message.class) ;
		return query.setParameter("d", doctor).getResultList();
	}

	/************************ done ************************/
	@Override
	public List<Message> getMessageByPatient(int patientId) {
		Patient patient = em.find(Patient.class, patientId) ;
		TypedQuery<Message> query = em.createQuery(
				"select m from Message m where m.patient= :p  ", Message.class) ;
		return query.setParameter("p", patient).getResultList();
	}

	/************************ done ************************/
	@Override
	public List<Message> getTodayMessages() {
		Date date = new Date() ; 
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int day = localDate.getDayOfMonth() ; 
		int month = localDate.getMonthValue() ; 
		int year = localDate.getYear() ; 
		TypedQuery<Message> query = em.createQuery(
				"select m from Message m where DAY(m.date) = :d and MONTH(m.date) = :m and YEAR(m.date) = :y ", Message.class) ;
		return query.setParameter("d", day).setParameter("m", month).setParameter("y", year).getResultList() ; 
	}

	
	/************************ done ************************/
	@Override
	public boolean getSeenMessage(int messageId){
		Message m = em.find(Message.class, messageId) ; 
		return m.isSeen() ;  
	}
	
	
	/************************ done ************************/
	@Override
	public JsonObject deleteMessage(int messageId) {
		try{
			Message m = em.find(Message.class, messageId) ; 
			m.setDoctor(null);
			m.setPatient(null);
			em.remove(m);
			return Json.createObjectBuilder().add("success", "deleted message").build();
			
		} catch (NoResultException e) {
			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
		}	
	}

	/************************ done ************************/
	@Override
	public Long countMessagesByPatient(int patientId) {
		Patient patient = em.find(Patient.class, patientId) ;
		TypedQuery<Long> query = em.createQuery(
				"select count(m) from Message m where m.patient = :p  ", Long.class) ;
		return query.setParameter("p", patient).getSingleResult() ;
	}

	/************************ done ************************/
	@Override
	public Long countMessagesByDoctor(int doctorId) {
		Doctor doctor = em.find(Doctor.class, doctorId) ; 
		TypedQuery<Long> query = em.createQuery(
				"select count(m) from Message m where m.doctor = :d  ", Long.class) ;
		return query.setParameter("d", doctor).getSingleResult() ;
	}
	

}
