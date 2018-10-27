package fr.epione.JAXRS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.entity.Calendrier;

import fr.epione.entity.MotifDoctor;
import fr.epione.interfaces.IdoctorServiceLocal;
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

	@Path("initialCalendar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialCalendar(@Context HttpServletRequest req) {
		return Response.ok(doctorService.initialCalendar(Utils.getIdUserFromSession(req))).build();
	}

	@Path("persoCalendar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response personnaliserCalendar(@Context HttpServletRequest req, @QueryParam(value = "date") Date date,
			HashMap<Integer, Integer> listHorairesPerso) {
		return Response.ok(doctorService.updateCalendar(Utils.getIdUserFromSession(req), date, listHorairesPerso))
				.build();
	}
	
	@Path("deleteJournee")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteJournee(@Context HttpServletRequest req, @QueryParam(value = "date") Date date){
		return Response.ok(doctorService.deleteJournee(Utils.getIdUserFromSession(req), date))
				.build();
	}

}
