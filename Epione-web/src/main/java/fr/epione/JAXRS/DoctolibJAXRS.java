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
	
	
	
	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test(){
		Doctor doctor = new Doctor() ; 
		doctor.setSpecialite("infirmier");
		Adresse adr = new Adresse() ; 
		adr.setVille("Le Pariés"); adr.setRue("fef");
		doctor.setAdresse(adr);
		doctor.setFirstName("Meéali"); doctor.setLastName("Ayedi");
		String url = getUrl(doctor)  ;
		return url;
	}
	
	@Path("add")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int add(){
		String specialite = "osteopathe" ; String ville = "tassin-la-demi-lune" ; String nom = "Flavien"  ; String prenom="Lamour";
		Doctor doctor = new Doctor() ; 
		Adresse adr = new Adresse() ; adr.setVille(ville);
		doctor.setSpecialite(specialite);doctor.setAdresse(adr);doctor.setFirstName(nom);doctor.setLastName(prenom);

		String url = getUrl(doctor)  ;
		Doctor DoctorFinal = getOne(url, doctor) ;
		int id = DS.addDoctor(DoctorFinal) ;
		return id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getUrl(Doctor doctor)
	{
		String specialite = doctor.getSpecialite(); 
		String ville = doctor.getAdresse().getVille();
		ville = ville.toLowerCase().replaceAll(" ", "-").replaceAll("ÿ", "y").replaceAll("'", "-").replaceAll("é", "e") ;
		String name = doctor.getFirstName().toLowerCase()+"-"+doctor.getLastName().toLowerCase();
		name = name.replaceAll("é", "e") ; 
		String url="www.doctolib.fr/"+specialite+"/"+ville+"/"+name;
		return  url ;
	}

	
	
	
	
	public Doctor getOne(String path, Doctor old)
	{
		Doctor doctor = new Doctor();
		
		
		
		
		List<String> liste = new ArrayList<String>() ;
		String url = path;
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
		
		
		  ////////////////////////////////////////////////////////
		  
		
		//Recuperation mode de paiement
		Elements PaiementData = fiche.select(".dl-profile-row-content").select(".dl-profile-row-section")
																		.select(".dl-profile-card-content")
																		.select(".dl-profile-text") ;
		String modePaiement=PaiementData.text();
		System.out.println(modePaiement);

		///////////////////////////////////////////////////////////////////////////
		
		//Recuperation adresse
		Elements adresseData= fiche.select("div.dl-profile-body-wrapper > div:nth-child(4) > div > div.dl-profile-card-content > div:nth-child(2)") ; 
		System.out.println(adresseData.text());
		
		doctor.setAdresse(old.getAdresse());
		
		
		/////////////////////////////////////////////////////////////////////////////
		
		//Recupartion des moyens de transport
		List<String> moyensTransport = new ArrayList<String>();
		Elements transport = fiche.select("body > div.dl-profile-bg.dl-profile > div.dl-profile-wrapper.dl-profile-responsive-wrapper > div.dl-profile-body-wrapper > div:nth-child(4) > div > div.dl-profile-card-content > div:nth-child(3) span ") ;
		for(Element p : transport)
		{
			moyensTransport.add(p.text()) ;
		}
		System.out.println(moyensTransport);
		
		/////////////////////////////////////////////////////////////////////////////
		
		//Recuperation du prix
		
		Elements prix = fiche.select(".dl-profile-fee-tag");
		System.out.println(prix.text());
		TarifDoctor tarif = new TarifDoctor() ;
		tarif.setDescription("prix consultation");
		Float f = Float.parseFloat(prix.text()) ;
		tarif.setTarif(f);
		
		/////////////////////////////////////////////////////////////////////////////

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
			Adresse adr = ParseAdresse(adresse) ; 
			
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
	
	
	
	
	


	
	
	
	
	
}
