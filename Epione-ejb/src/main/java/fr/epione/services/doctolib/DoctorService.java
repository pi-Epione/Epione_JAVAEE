package fr.epione.services.doctolib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;
import fr.epione.entity.ExpertiseDoctor;
import fr.epione.entity.FormationDoctor;
import fr.epione.entity.User;
import fr.epione.interfaces.doctolib.IDoctorServiceLocal;
import fr.epione.interfaces.doctolib.IDoctorServiceRemote;

@Stateless
public class DoctorService implements IDoctorServiceLocal,IDoctorServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;
	ScrapServices Services ;
	
	@Override
	public int addDoctor(Doctor doctor) {

		em.persist(doctor);
		sendMail(doctor.getEmail());
		return doctor.getId();
		
	}

	@Override
	public List<Doctor> getDoctors() {
		
		/*List<Doctor> liste= Services.getDoctors();
		return liste;*/
		List<Doctor> liste = new ArrayList<Doctor>() ; 
		String url = "https://www.doctolib.fr/infirmier/france";
		  Document doc;
		try {
			doc = Jsoup.connect(url).userAgent("Opera").get();

		  Elements paragraphs = doc.getElementsByClass("dl-search-result-presentation") ; 
		  for(Element p : paragraphs)
		  {
			String name = p.select(".dl-search-result-name").text();
			//String objet = p.select(".dl-search-result-subtitle").text();
			String adresse = p.select(".dl-text").text();
			String image = p.select("img").attr("src") ; 
			System.out.println(name + image );
			Doctor doctor = new Doctor() ; doctor.setFirstName(name);
			liste.add(doctor) ;
		  }
		return  liste;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();return null;
		}
	}

	@Override
	public int ajoutDemande(DemandeDoctolib demande) {
		if(checkIfDemandeExist(demande))
		{
			return 0 ;
		}
		else {
			em.persist(demande);
			return demande.getId();
		}
		
	}





	@Override
	public Adresse ParseAdresse(String adresse)
	{
		Adresse adr = new Adresse() ; 
		
		String parts[] = adresse.split(" ") ;
		
		adr.setNumAppart(parts[0]);
		String rue ="" ;
		for(int i=1 ; i<parts.length ; i++)
		{
			if (stringContainsNumber(parts[i]))
			{
				adr.setRue(rue);
				adr.setCodePostal(parts[i]);
				String ville ="" ; 
				for(int j=i+1 ; j<parts.length ; j++)
				{
					ville+=parts[j];
				}
				adr.setVille(ville);
			}
			rue+=parts[i]+" " ; 

		}
		
		return adr;
	}
	
	
	public boolean stringContainsNumber( String s )
	{
	    return Pattern.compile( "[0-9]" ).matcher( s ).find();
	}

	@Override
	public List<DemandeDoctolib> getDemandes() {

		String jpql = "SELECT d FROM DemandeDoctolib d " ; 
		Query query = em.createQuery(jpql) ; 
		return query.getResultList(); 
	}

	@Override
	public void AddExpertises(List<ExpertiseDoctor> liste) {

		for (ExpertiseDoctor e : liste)
		{
			em.persist(e);
		}
	}

	@Override
	public void addFormations(List<FormationDoctor> liste) {

		for ( FormationDoctor f : liste)
		{
			em.persist(f);
		}
	}

	@Override
	public void AffecterExpertise(List<ExpertiseDoctor> liste, Doctor doctor) {

		List<ExpertiseDoctor> expertises = new ArrayList<ExpertiseDoctor>() ; 
		
		for ( ExpertiseDoctor e : liste )
		{
			expertises.add(e);
			System.out.println(e);
		}
		System.out.println("doctor is " + doctor );
		System.out.println(expertises);
		doctor.setExpertises(expertises);

		
	}

	@Override
	public void affecterFormations(List<FormationDoctor> liste, Doctor doctor) {
		
		List<FormationDoctor> formations = new ArrayList<FormationDoctor>() ; 
		
		for( FormationDoctor f : liste)
		{
			formations.add(f) ; 
		}
		doctor.setFormations(formations);
		
	}

	@Override
	public List<Doctor> getAllDoctors() {

		String jpql = "SELECT d FROM Doctor d where d.doctolib=true " ; 
		Query query = em.createQuery(jpql) ; 
		return query.getResultList(); 
	}

	@Override
	public Doctor getDoctor(int id) {
		
		Doctor d= em.find(Doctor.class, id) ; 
		return d;
	}
	
	
public void sendMail(String email)
{
	 try{
         String host = "smtp.gmail.com";
         String from = "medali.ayedi@esprit.tn";
         String pass = "Medalicss1231996";
         Properties props = System.getProperties();
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", host);
         props.put("mail.smtp.user", from);
         props.put("mail.smtp.password", pass);
         props.put("mail.smtp.port", "587");
         props.put("mail.smtp.auth", "true");
         String to=email ; 
         Session session = Session.getDefaultInstance(props, null);
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         InternetAddress toAddress = new InternetAddress(to);

	   message.addRecipient(Message.RecipientType.TO, toAddress);
    message.setSubject("sending in a group");
    message.setText("test2");
    Transport transport = session.getTransport("smtp");
    transport.connect(host, from, pass);
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
}
catch(Exception e){
    e.getMessage();
}

}

@Override
public int deleteDemande(DemandeDoctolib demande) {
	System.out.println("this is it" +demande.getEmail());
	 TypedQuery<DemandeDoctolib> query = em.createQuery(
		        "SELECT c FROM DemandeDoctolib c WHERE c.email = :email", DemandeDoctolib.class);
	 DemandeDoctolib d = query.setParameter("email", demande.getEmail()).getSingleResult();
	em.remove(d);
	return d.getId();
}

public Boolean checkIfDemandeExist(DemandeDoctolib demande)
{

 TypedQuery<DemandeDoctolib> query = em.createQuery(
	        "SELECT c FROM DemandeDoctolib c WHERE c.email = :email", DemandeDoctolib.class);
int size = query.setParameter("email", demande.getEmail()).getResultList().size();
if(size==0) return false;
else {
	return true;
}
}

}
