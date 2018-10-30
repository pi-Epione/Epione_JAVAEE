package fr.epione.JAXRS;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.interfaces.IanalyticsDoctorLocal;
import fr.epione.utils.Utils;


@Path("analyticsDoctor")
@RequestScoped
public class analyticsDoctorRessources {

	@EJB
	IanalyticsDoctorLocal analyticsDoctorService;
	
	@Path("nbrPatientTraite")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNbrPatientTraite(@Context HttpServletRequest req){
		return Response.ok(analyticsDoctorService.nbrPatientTraite(Utils.getIdUserFromSession(req))).build();
	}
}