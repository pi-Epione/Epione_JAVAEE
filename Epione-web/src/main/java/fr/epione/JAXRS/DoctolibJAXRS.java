package fr.epione.JAXRS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.epione.JAXRS.TestMedali.Services;
import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;
import fr.epione.entity.TarifDoctor;
import fr.epione.entity.User;
import fr.epione.interfaces.doctolib.IDoctorServiceLocal;



@Path("doctolib")
@RequestScoped
public class DoctolibJAXRS {

	@Inject
	IDoctorServiceLocal DS;
	
	Services services ;
	
	@Path("addDoctor")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDoctor(Doctor doctor){
		int id = DS.addDoctor(doctor) ; 
		return Response.ok(id).build();
	}
	
	
	@Path("ajoutDemande")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ajoutDemande(DemandeDoctolib demande){
		int id = DS.ajoutDemande(demande) ; 
		return Response.ok(id).build();
	}
	
	@Path("getDemande")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDemande() {
		
		List<DemandeDoctolib> liste = DS.getDemandes();
		GenericEntity<List<DemandeDoctolib>> entity = new GenericEntity<List<DemandeDoctolib>>(liste){};
		return Response.ok(entity).build();

	}
		
	
	@Path("getDoctors")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctors(){
		//List<Doctor> list = DS.getDoctors();
		List<Doctor> list = new ArrayList<Doctor>() ;
		 list= services.getDoctors();
		GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(list){};
		return Response.ok(entity).build();
	}
	
	
	
	
	
	@Path("getAll")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		
		  GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(getListe()){};
			return Response.ok(entity).build();			
	}
	
	
	
	
	
	
	@Path("AcceptDemande")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AcceptDemande(DemandeDoctolib demande){
		
		//this is supposed to be Input from Front but for test i will fill the object 
		demande.setFirstName("Flavien"); demande.setLastName("Lamour"); demande.setSpecialite("osteopathe");
		demande.setVille("tassin-la-demi-lune");
		/////////////////////////////////////////////////////////////////////////////
		String url = parseURL(demande) ; 
		
		Doctor d = getOne(url, demande);
		d.setDoctolib(true);
		int id = DS.addDoctor(d) ; 
		return Response.ok(id).build();
	}
	
	
	
	
	
	public String parseURL(DemandeDoctolib demande)
	{
		String specialite = demande.getSpecialite();
		String ville = demande.getVille();
		ville = ville.toLowerCase().replaceAll(" ", "-").replaceAll("ÿ", "y").replaceAll("'", "-").replaceAll("é", "e") ;
		String name = demande.getFirstName().toLowerCase()+"-"+demande.getLastName().toLowerCase();
		name = name.replaceAll("é", "e") ; 
		String url ="https://www.doctolib.fr/"+specialite+"/"+ville+"/"+name;
		return url ;
	}
	
	
	


	
	
	
	public Doctor getOne(String path, DemandeDoctolib demande)
	{
		Doctor doctor = new Doctor();
		
		
		
		
		List<String> liste = new ArrayList<String>() ;
		String url = path;
		url.replaceAll(" ",	"") ;
		Document fiche;
		try {
			fiche = Jsoup.connect(url).userAgent("Opera").get();
		
	
		/////////////////////////////////////////////////////////
		
		//Recuperation de l'expertise du medecin 
		String expertise ="" ;  int testexp = 0 ; 
		Elements expertiseData = fiche.getElementsByClass("dl-profile-skill-chip") ; 
		for(Element p : expertiseData)
		{
			if(testexp == 0 ) {
				expertise = p.text() ;
			}
			else {
			expertise += " , "+p.text() ; }
			testexp++ ; 
		}
		  System.out.println(expertise);
		
		
		//Recuperation adresse
		Elements adresseData= fiche.select("div.dl-profile-body-wrapper > div:nth-child(4) > div > div.dl-profile-card-content > div:nth-child(2)") ; 
		System.out.println(adresseData.text());
		
		doctor.setAdresse(DS.ParseAdresse(adresseData.text()));
			
		
		//Recuperation du prix
		
		Elements prix = fiche.select(".dl-profile-fee-tag");
		System.out.println(prix.text());
		TarifDoctor tarif = new TarifDoctor() ;
		tarif.setDescription("prix consultation");
		String parts[] = prix.text().split(" "); 
		Float f = Float.parseFloat(parts[0]) ;
		tarif.setTarif(f);
		doctor.getTarifs().add(tarif);
		
		/////////////////////////////////////////////////////////////////////////////

		Elements pres = fiche.select("div.dl-profile-card-content > div.dl-profile-text.js-bio.dl-profile-bio"); 

		
		doctor.setPresentation(pres.text());
		doctor.setFirstName(demande.getFirstName());
		doctor.setLastName(demande.getLastName());
		doctor.setSpecialite(demande.getSpecialite());
		return doctor ;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

		
	}
	
	
	
	public List<Doctor> getListe()
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
			String[] parts = name.split(" ") ;
			String adresse = p.select(".dl-text").text();
			String image = p.select("img").attr("src") ; 
			System.out.println(name + image );
			Adresse adr = DS.ParseAdresse(adresse) ; 
			
			Doctor doctor = new Doctor() ; doctor.setFirstName(parts[1]); doctor.setLastName(parts[2]);
			doctor.setAdresse(adr);
			liste.add(doctor) ;
		  }
		  return liste ;
		  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();return null;
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	
	
	
	
}
