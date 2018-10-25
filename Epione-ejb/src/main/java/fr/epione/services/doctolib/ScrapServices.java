package fr.epione.services.doctolib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.epione.entity.Doctor;

public class ScrapServices {

	public List<Doctor> getDoctors()
	{

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
