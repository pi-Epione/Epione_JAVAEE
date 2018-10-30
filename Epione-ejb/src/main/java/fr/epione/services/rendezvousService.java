package fr.epione.services;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import javax.xml.soap.MessageFactory;

import fr.epione.entity.Doctor;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;
import fr.epione.entity.State;
import fr.epione.entity.User;
import fr.epione.interfaces.IrendezvousServiceLocal;
import fr.epione.interfaces.IrendezvousServiceRemote;


@Stateless
public class rendezvousService implements IrendezvousServiceLocal, IrendezvousServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;
	
	@Resource(name = "java:jboss/mail/gmail")
    private Session session;
	
	public static final String ACCOUNT_SID = "AC52caec72c2867b449005d54b0e369e4b";
    public static final String AUTH_TOKEN = "b6d889e91f8b10aedbf0b7426cdf05ba";
	
	@Override
	public JsonObject addRendezVous(RendezVous rdv) {
		try{
		em.persist(rdv); 
		if(rdv.getPatient()!= null){
		send("weja.molka@gmail.com", "rendez Vous", "M(Mme)"+rdv.getPatient().getId());}
		return Json.createObjectBuilder().add("success", "rendez vous ajouté").build();
		
	} catch (NoResultException e) {
		return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
	}
	}

	@Override
	public int updateRendezVous(RendezVous rdv) {
		RendezVous r = em.find(RendezVous.class, rdv.getIdentifiant() ) ; 
		r.setDate(rdv.getDate());
		r.setMedecin(rdv.getMedecin());
		r.setMotif(rdv.getMotif());
		r.setPatient(rdv.getPatient());
		r.setReason(rdv.getReason());
		r.setState(rdv.getState());
		return r.getIdentifiant();
	}

	@Override
	public JsonObject cancelRendezVous(int rdvId) {
		try{
			RendezVous r = em.find(RendezVous.class, rdvId ) ; 
			r.setMedecin(null);
			r.setMotif(null);
			r.setPatient(null);
			em.remove(r);
			return Json.createObjectBuilder().add("success", "rendez vous supprimé").build();
			
		} catch (NoResultException e) {
			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
		}
	}

	/*@Override
	public JsonObject addMotifDoctor(MotifDoctor motif, int rendezVousId) {
		try{
			RendezVous r = em.find(RendezVous.class, rendezVousId ) ; 
			r.setMotif(motif);
			return Json.createObjectBuilder().add("success", "motif ajouté au rendezvous").build();
			
		} catch (NoResultException e) {
			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
		}
	}*/

	@Override
	public List<RendezVous> getListRendezVous() {
		return em.createQuery("select r from RendezVous r", RendezVous.class).getResultList();
	}

	@Override
	public List<RendezVous> getListRendezVousByMotif(int motifId) {
		TypedQuery<RendezVous> query = em.createQuery(
				"select r from RendezVous r where r.motif= : '"+motifId+"'  ", RendezVous.class) ;
		return query.getResultList();
	}

	@Override
	public List<RendezVous> getListRendezVousByDoctor(int doctorId) {
		TypedQuery<RendezVous> query = em.createQuery(
				"select r from RendezVous r where r.Medecin=  '"+doctorId+"'  ", RendezVous.class) ;
		return query.getResultList();
	}

	@Override
	public List<RendezVous> getListRendezVousByPatient(int patientId) {
		TypedQuery<RendezVous> query = em.createQuery(
				"select r from RendezVous r where r.patient = '" +patientId+"' ", RendezVous.class) ;
		return query.getResultList();
	}

	@Override
	public int confimRendezVous(int rendezvousId) {
		    
			RendezVous r = em.find(RendezVous.class, rendezvousId ) ;
			if(r != null){
			r.setState(State.accepted);
			}
			return rendezvousId;
	}

	@Override
	public List<RendezVous> getStateRendezVous(int doctorId) {
		TypedQuery<RendezVous> query = em.createQuery(
				"select r from RendezVous r where r.state = :s and r.Medecin= '"+doctorId+"'", RendezVous.class) ;
		return query.setParameter("s", State.waiting).getResultList();
	}
	
	@Override
	public void send(String address, String topic, String textMessage) {
	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("app-srv@example.com"));
	        Address toAddress = new InternetAddress(address);
	        message.addRecipient(Message.RecipientType.TO, toAddress);
	        message.setSubject(topic);
	        message.setContent(textMessage,  "text/html");
	        Transport.send(message);


	    } catch (MessagingException e) {
	    	Logger.getLogger(rendezvousService.class.getName()).log(Level.WARNING, "Cannot send mail", e);
	    }
	}
	
	
//	public void sendSMS(String numero, String msg){
//		try{
//		AuthMethod auth = new TokenAuthMethod("cae3ac45", "pjigkFHSRClco4cX");
//        NexmoClient client = new NexmoClient(auth);
//        TextMessage message = new TextMessage("EpioneSMS", numero, msg);
//        //There may be more than one response if the SMS sent is more than 160 characters.
//        SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(message);
////          for (SmsSubmissionResult response : responses) {
////          System.out.println(response);
////        } 
//		} catch (Exception e) {
//			System.out.println("une erreur est survenue");
//		}
//	}

	

	
	
}
