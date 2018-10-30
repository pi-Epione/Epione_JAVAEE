package fr.epione.JAXRS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.entity.Calendrier;
import fr.epione.entity.Doctor;
import fr.epione.entity.Horaires;
import fr.epione.entity.Journee;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.User;
import fr.epione.interfaces.IdoctorServiceLocal;
import fr.epione.services.userService;
import fr.epione.utils.Utils;

@Path("doctors")
@RequestScoped
public class DoctorRessources {

	@EJB
	IdoctorServiceLocal doctorService;
	
	
	@Path("addMotif")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMotif(MotifDoctor motif) {
		return Response.ok(doctorService.addMotif(motif)).build();
	}

	@Path("initialMotifs")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialMotifs(List<Integer> idsMotif, @Context HttpServletRequest req) {
		return Response.ok(doctorService.initialMotifs(idsMotif, Utils.getIdUserFromSession(req))).build();
	}

	@Path("updateMotifs")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMotifs(List<Integer> idsMotif, @Context HttpServletRequest req) {
		return Response.ok(doctorService.updateMotif(idsMotif, Utils.getIdUserFromSession(req))).build();
	}

	@Path("getMotif")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMotifById(@QueryParam(value = "idMotif") int idMotif) {
		return Response.ok(doctorService.getMotifById(idMotif)).build();
	}

	@Path("getListMotifs")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListMotifsByDoctor(@Context HttpServletRequest req) {
		return Response.ok(doctorService.getListMotifsByDoctor(Utils.getIdUserFromSession(req))).build();
	}

	@Path("getAllMotifs")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMotifs() {
		return Response.ok(doctorService.getAllMotifs()).build();
	}
	
	@Path("getDoctorsBySpecialite")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctorsBySpecialite(@QueryParam(value="specialite")String specialite) {
		List<Doctor> listDoctors = doctorService.getDoctorBySpeciality(specialite);
		GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(listDoctors) {
		};
		return Response.ok(entity).build();
	}
	
	@Path("getDoctorsDisponible")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctorsDisponibleByDate(@QueryParam(value="date")Date date) {
		List<Doctor> listDoctors = doctorService.getDoctorsDisponibleByDate(date);
		if(listDoctors.size()!=0){
			GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(listDoctors) {
			};
			return Response.ok(entity).build();
		}else{
			return Response.ok("Pas de doctor disponible").build();
		}
		
	}
	
	@Path("getCalendar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCalendar(@Context HttpServletRequest req) {
		Calendrier calendrier = doctorService.getCalendar(Utils.getIdUserFromSession(req));
		List<Journee> listJournees = calendrier.getListJournees();
		GenericEntity<List<Journee>> entity = new GenericEntity<List<Journee>>(listJournees) {
		};
		return Response.ok(entity).build();
	}

	@Path("initialCalendar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialCalendar(@Context HttpServletRequest req) {
		return Response.ok(doctorService.initialCalendar(Utils.getIdUserFromSession(req))).build();
	}

	@Path("addJournee")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response personnaliserCalendar(@Context HttpServletRequest req, @QueryParam(value = "date") Date date,
			HashMap<Integer, Integer> listHorairesPerso) {
		return Response.ok(doctorService.addJournee(Utils.getIdUserFromSession(req), date, listHorairesPerso))
				.build();
	}
	
	@Path("deleteJournee")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteJournee(@Context HttpServletRequest req, @QueryParam(value = "date") Date date){
		if (doctorService.deleteJournee(Utils.getIdUserFromSession(req), date)) {
			return Response.ok("Journée effacée").build();
		}else{
			return Response.ok("cette journée contient des rendez vous").build();
		}
	}
	
	@Path("deleteHoraire")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHoraire(@Context HttpServletRequest req, @QueryParam(value = "date") Date date){
		if (doctorService.deleteHoraires(Utils.getIdUserFromSession(req), date)) {
			return Response.ok("Horaire effacée").build();
		}else{
			return Response.ok("Impossible d'annuler cet horaire").build();
		}
	}

	@Path("getHorairesJournee")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHorairesJournee(@Context HttpServletRequest req, @QueryParam(value = "date") Date date){
		List<Horaires> horaires = new ArrayList<>();
		if(doctorService.getHorairesJournees(Utils.getIdUserFromSession(req),date)==null){
			return Response.ok("date n'existe pas").build();
		}else{
			List<Horaires> listHoraires =doctorService.getHorairesJournees(Utils.getIdUserFromSession(req),date);
//			for(Horaires h : listHoraires){
//				Horaires h2 = new Horaires();
//				h2.setId(h.getId());
//				h2.setTime(h.getTime());
//				h2.setDisponible(h.getDisponible());
//				
//				horaires.add(h2);
//			}
			
			return Response.ok(listHoraires).build();
		}
	}
}
