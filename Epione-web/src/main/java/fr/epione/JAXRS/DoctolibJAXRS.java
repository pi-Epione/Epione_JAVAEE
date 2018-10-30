package fr.epione.JAXRS;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.jsoup.select.Evaluator.IsEmpty;

import fr.epione.JAXRS.TestMedali.Services;
import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;
import fr.epione.entity.ExpertiseDoctor;
import fr.epione.entity.FormationDoctor;
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
		if(id==0) return Response.ok("demande existe").build();
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
		
	
	
	@Path("getAll")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		
		  GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(getListe()){};
			return Response.ok(entity).build();			
	}
	
	@Path("getFormations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFormations(){
		String url = "https://www.doctolib.fr/dentiste/joinville/rachwan-balgone" ; 
		List<FormationDoctor> lis = getFormation(url) ;
		GenericEntity<List<FormationDoctor>> entity = new GenericEntity<List<FormationDoctor>>(lis){};
			return Response.ok(entity).build();			
	}
	@Path("getExpertise")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExpertises(){
		String url = "https://www.doctolib.fr/chirurgien/saint-pierre-du-mont/gerold-schroder" ; 
		List<ExpertiseDoctor> lis = getExpertise(url) ;
		GenericEntity<List<ExpertiseDoctor>> entity = new GenericEntity<List<ExpertiseDoctor>>(lis){};
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
		List<ExpertiseDoctor> expertises= getExpertise(url) ;
		List<FormationDoctor> formations= getFormation(url); 
		if(expertises.isEmpty() && formations.isEmpty() && d.getTarifs().isEmpty())
			return Response.ok("Doctor not available").build();
		DS.AddExpertises(expertises);
		DS.addFormations(formations);
		DS.AffecterExpertise(expertises, d);
		DS.affecterFormations(formations, d);
		Date date = new Date();
		d.setDateCreation(date);
		d.setDoctolib(true);
		d.setIsEnable(true);
		d.setEmail(demande.getEmail());
		int id = DS.addDoctor(d) ; 
		int x = DS.deleteDemande(demande) ;
		if(id==0) return Response.ok("Doctor already exist").build();
		else {			
			return Response.ok(id).build();
		}
	}
	
	@Path("RejectDemande")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response Reject(DemandeDoctolib demande){
		
		int test=DS.deleteDemande(demande) ;
		if(test==0)
			return Response.ok("demande does not exist").build();
		else {
			return Response.ok("demande deleted").build();
		}
	}
	
	@Path("deleteDoctor")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDoctor(Doctor doctor){
		
		Boolean test=DS.deleteDoctor(doctor) ;
		if(test==true)
			return Response.ok("doctor deleted").build();
		if(test==false) {
			return Response.ok("doctor does not exist").build();
		}
		else { 
			return Response.ok("error").build();
		}
	}
	
	

	
	
	
	
	@Path("testjoin")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response testjoin() {
		
		List<DemandeDoctolib> liste = DS.getDemandes();
		GenericEntity<List<DemandeDoctolib>> entity = new GenericEntity<List<DemandeDoctolib>>(liste){};
		return Response.ok(entity).build();

	}
	@Path("getDoctorsDB")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctorsDB() {
		
		List<Doctor> liste = DS.getAllDoctors();
		GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(liste){};
		return Response.ok(entity).build();

	}
	
	@Path("DoctolibPercentage")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response DoctolibPercentage() {
		

		double x = DS.DoctolibPercentage();
		return Response.ok(x).build();

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
		
	
		//Recuperation adresse
		Elements adresseData= fiche.select("div.dl-profile-body-wrapper > div:nth-child(4) > div > div.dl-profile-card-content > div:nth-child(2)") ; 
		System.out.println(adresseData.text());
		
		doctor.setAdresse(DS.ParseAdresse(adresseData.text()));
			
		
		//Recuperation du prix
		
		Elements prix = fiche.select(".dl-profile-fee-tag");
		System.out.println(prix.text());
		TarifDoctor tarif = new TarifDoctor() ;
		if(!prix.text().isEmpty()) {
		tarif.setDescription("prix consultation");

		String parts[] = prix.text().split(" "); 
		Float f = Float.parseFloat(parts[0]) ;
		tarif.setTarif(f);
		doctor.getTarifs().add(tarif);}
		
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
		try {
		for ( int i = 1 ; i<2 ; i++ )
		{
		String url = "https://www.doctolib.fr/masseur-kinesitherapeute?page="+i+" ";
		  Document doc;
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
			
			Doctor doctor = new Doctor() ;
			if(parts[0].contains("Mme") || parts[0].contains("M.") || parts[0].contains("Dr")) {
			doctor.setFirstName(parts[1]); doctor.setLastName(parts[2]);
			}
			else {
				doctor.setFirstName(parts[0]); doctor.setLastName(parts[1]);
			}
			doctor.setAdresse(adr);
			liste.add(doctor) ;
		  }
			}
		  return liste ;
		  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();return null;
			}
	}
	
	
	List<FormationDoctor> getFormation(String url)
	{
		
		List<FormationDoctor> liste = new ArrayList<FormationDoctor>();
		Document fiche;
		try {
		fiche = Jsoup.connect(url).userAgent("Opera").get();
		Elements test = fiche.select("body > div.dl-profile-bg.dl-profile > div.dl-profile-wrapper.dl-profile-responsive-wrapper > div.dl-profile-body-wrapper > div:nth-child(6) > div:nth-child(3) > div.dl-profile-card-content"); 
		String partsF[] = test.text().split(" ") ;
		try {
		for (int i=0 ; i<partsF.length ; i++)
		{
			if(partsF[i].contains("Formations"))
				{
				Elements data = test.select(">div") ; 
				for (Element p: data)
				{
					FormationDoctor f = new FormationDoctor();
					String partsData[] = p.text().split(" ") ;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");		
					Date d;
					d = sdf.parse(partsData[0]);
					f.setDate(d);
				/*	String desc = "" ; 
					for( int j=1 ; j<partsData.length ; j++)
					{
						desc+=partsData[i] ;
						
					}*/
					f.setDiplome(p.text());
					System.out.println(f);
					liste.add(f);
					System.out.println("element : "+p.text());
				}
				break ;
				}
			else {System.out.println("false");
			}
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return liste; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	
	
	
	List<ExpertiseDoctor> getExpertise(String url)
	{
		List<ExpertiseDoctor> liste = new ArrayList<ExpertiseDoctor>();
		Document fiche;
		try {
			fiche = Jsoup.connect(url).userAgent("Opera").get();


		String expertise ="" ;  int testexp = 0 ; 
		Elements expertiseData = fiche.getElementsByClass("dl-profile-skill-chip") ; 
		for(Element p : expertiseData)
		{
			ExpertiseDoctor exp = new ExpertiseDoctor() ; 
			exp.setNom(p.text());
			liste.add(exp) ;
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		return liste ;
	}
	
	
	
	
	
	
	


	
	
	
	
	
}
