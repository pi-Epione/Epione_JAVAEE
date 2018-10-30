//package fr.epione.services;
//
//import java.io.BufferedReader;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.annotation.Resource;
//import javax.ejb.Asynchronous;
//import javax.ejb.Lock;
//import javax.ejb.LockType;
//import javax.ejb.Stateless;
//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.mail.Address;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.persistence.EntityManager;
//import javax.persistence.EnumType;
//import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import javax.sound.midi.Soundbank;
//import javax.swing.JOptionPane;
//import javax.xml.soap.MessageFactory;
//
//import com.nexmo.client.NexmoClient;
//import com.nexmo.client.auth.AuthMethod;
//import com.nexmo.client.auth.TokenAuthMethod;
//import com.nexmo.client.sms.SmsSubmissionResult;
//import com.nexmo.client.sms.messages.TextMessage;
//import com.notification.NotificationFactory;
//import com.notification.NotificationFactory.Location;
//import com.notification.NotificationManager;
//import com.notification.manager.SimpleManager;
//import com.notification.types.TextNotification;
//import com.theme.ThemePackagePresets;
//import com.utils.Time;
//
//import fr.epione.entity.Doctor;
//import fr.epione.entity.MotifDoctor;
//import fr.epione.entity.Patient;
//import fr.epione.entity.RendezVous;
//import fr.epione.entity.State;
//import fr.epione.entity.User;
//import fr.epione.interfaces.IrendezvousServiceLocal;
//import fr.epione.interfaces.IrendezvousServiceRemote;
//
//
//@Stateless
//public class rendezvousService implements IrendezvousServiceLocal, IrendezvousServiceRemote {
//
//	@PersistenceContext(unitName = "epione")
//	EntityManager em;
//	
//	@Resource(name = "java:jboss/mail/gmail")
//    private Session session;
//	
//	public static final String ACCOUNT_SID = "AC52caec72c2867b449005d54b0e369e4b";
//    public static final String AUTH_TOKEN = "b6d889e91f8b10aedbf0b7426cdf05ba";
//	
//    
//    /************************ done ************************/
//	@Override
//	public JsonObject addRendezVous(RendezVous rdv) {
//		try{
//			if(rdv.getPatient()==null | rdv.getMedecin()==null){
//				return Json.createObjectBuilder().add("error", "vous devez selectionner un docteur et un patient").build();
//			}
//			sendSMS("+21625120834", "Bonjour M(Mme) "+rdv.getMedecin().getFirstName()+"Vous avez un "
//					+ "nouveau rendez vous à confirmer le "+rdv.getDate()+"!  "
//					+ " Merci");
//		em.persist(rdv); 
//		rdv.setState(State.waiting);
//		Patient p = em.find(Patient.class, rdv.getPatient().getId()) ; 
//		p.getListeRendezVous().add(rdv) ; 
//		if(rdv.getMotif()!=null){
//		MotifDoctor m = em.find(MotifDoctor.class, rdv.getMotif().getId()) ;
//		m.getListeRendezVous().add(rdv) ;}
//		Doctor d = em.find(Doctor.class, rdv.getMedecin().getId()) ; 
//		d.getListeRendezVous().add(rdv) ; 
//		return Json.createObjectBuilder().add("success", "ajout rendez vous").build();
//		
//	} catch (NoResultException e) {
//		return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
//	}
//	}
//
//	/************************ done ************************/
//	@Override
//	public int updateRendezVous(RendezVous rdv) {
//		RendezVous r = em.find(RendezVous.class, rdv.getIdentifiant() ) ;
//		if(rdv.getDate()!=null){r.setDate(rdv.getDate());}
//		if(rdv.getMedecin()!=null){r.setMedecin(rdv.getMedecin());};
//		if(rdv.getMotif()!=null){r.setMotif(rdv.getMotif());} ;
//		if(rdv.getPatient()!=null){r.setPatient(rdv.getPatient());};
//		if(rdv.getReason()!=null){r.setReason(rdv.getReason());}; 
//		if(rdv.getState()!=null){r.setState(rdv.getState());};
//		return r.getIdentifiant();
//	}
//
//	/************************ done ************************/
//	@Override
//	public JsonObject cancelRendezVous(int rdvId) {
//		try{
//			RendezVous r = em.find(RendezVous.class, rdvId ) ; 
//			r.setMedecin(null);
//			r.setMotif(null);
//			r.setPatient(null);
//			em.remove(r);
//			return Json.createObjectBuilder().add("success", "deleted rendez vous").build();
//			
//		} catch (NoResultException e) {
//			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
//		}
//	}
//
//	/************************ done ************************/
//	@Override
//	public JsonObject addMotifDoctor(MotifDoctor motif, int rendezVousId) {
//		try{
//			em.persist(motif);
//			RendezVous r = em.find(RendezVous.class, rendezVousId ) ; 
//			r.setMotif(motif);
//			return Json.createObjectBuilder().add("success", "motif added to the rendez vous").build();
//			
//		} catch (NoResultException e) {
//			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
//		}
//	}
//
//	/************************ done ************************/
//	@Override
//	public List<RendezVous> getListRendezVous() {
//		return em.createQuery("select r from RendezVous r", RendezVous.class).getResultList();
//	}
//
//	/************************ done ************************/
//	@Override
//	public List<RendezVous> getListRendezVousByMotif(int motifId) {
//		MotifDoctor motif = em.find(MotifDoctor.class, motifId) ;
//		TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r where r.motif=:m ", RendezVous.class) ;
//		return query.setParameter("m", motif).getResultList();
//	}
//
//	/************************ done ************************/
//	@Override
//	public List<RendezVous> getListRendezVousByDoctor(int doctorId) {
//		TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r where r.Medecin=  '"+doctorId+"'  ", RendezVous.class) ;
//		return query.getResultList();
//	}
//
//	/************************ done ************************/
//	@Override
//	public List<RendezVous> getListRendezVousByPatient(int patientId) {
//		TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r where r.patient = '" +patientId+"' ", RendezVous.class) ;
//		return query.getResultList();
//	}
//
//	/************************ done ************************/
//	@Override
//	public int confimRendezVous(int rendezvousId) {
//		    
//			RendezVous r = em.find(RendezVous.class, rendezvousId ) ;
//			if(r != null){
//			r.setState(State.accepted);
//			if(r.getPatient()!= null){
//			sendMail(r.getPatient().getEmail(), "rendez Vous", "Bonjour M(Mme) "+r.getPatient().getFirstName()+", "+"votre rendez vous "
//					+ "avec le docteur "+r.getMedecin().getLastName()+" "+r.getMedecin().getFirstName()+" a été confirmé! Merci");}
//			}
//			return rendezvousId;
//	}
//	
//	/************************ done ************************/
//	@Override
//	public void sendMail(String address, String topic, String textMessage) {
//	    try {
//	        Message message = new MimeMessage(session);
//	        message.setFrom(new InternetAddress("app-srv@example.com"));
//	        Address toAddress = new InternetAddress(address);
//	        message.addRecipient(Message.RecipientType.TO, toAddress);
//	        message.setSubject(topic);
//	        message.setContent(textMessage,  "text/html");
//	        Transport.send(message);
//
//
//	    } catch (MessagingException e) {
//	    	Logger.getLogger(rendezvousService.class.getName()).log(Level.WARNING, "Cannot send mail", e);
//	    }
//	}
//
//	/************************ done ************************/
//	@Override
//	public List<RendezVous> getStateRendezVous(int doctorId) {
//		TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r where r.state = :s and r.Medecin= '"+doctorId+"'", RendezVous.class) ;
//		return query.setParameter("s", State.waiting).getResultList();
//	}
//	
//	/************************ done ************************/
//	@Override
//	public JsonObject getDoctorAvailability(int doctorId,String dateS){
//		try{
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//	        Date date = new Date();
//			try {
//				date = df.parse(dateS);
//			} catch (ParseException e1) {
//				e1.printStackTrace();
//			}
//			TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r left join r.Medecin d where d.id= '"+doctorId+"' and r.date= :dd  ", RendezVous.class) ;
//		 List<RendezVous> listeR = query.setParameter("dd",date).getResultList();
//		 int i = 0 ; 
//	    for(RendezVous r : listeR){
//	    	if(r!=null){
//	    		i++ ;  	} }
//		 if(i!=0){
//			 return Json.createObjectBuilder().add("availability", "doctor is not available").build();
//		 }else {
//			 return Json.createObjectBuilder().add("availability", "doctor is available").build();
//		 }
//		 }
//		catch (NoResultException e) {
//			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
//		}
//	}
//	
//	/************************ done ************************/
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
//
//	
//
//	
//	
//}
