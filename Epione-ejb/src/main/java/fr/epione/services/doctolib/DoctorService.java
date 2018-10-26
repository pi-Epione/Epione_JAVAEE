package fr.epione.services.doctolib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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





}
