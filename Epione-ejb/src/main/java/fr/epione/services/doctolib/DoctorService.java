package fr.epione.services.doctolib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;
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
		em.persist(demande);
		return demande.getId();
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
	
	
	

}
