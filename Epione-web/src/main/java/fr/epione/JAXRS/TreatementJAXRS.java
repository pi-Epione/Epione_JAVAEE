package fr.epione.JAXRS;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;
import fr.epione.interfaces.ItreatementServiceLocal;


@Path("Path")
@RequestScoped
public class TreatementJAXRS {
	@PersistenceContext
	EntityManager em;
	@Inject
	ItreatementServiceLocal TreatmentService;	
	@POST
	@javax.ws.rs.Produces(MediaType.APPLICATION_XML)
	@Path("/addTreatement/{description}/{duration}/{result}/{idPath}")
	public Response addPath (@PathParam(value="description")String description
			,@PathParam(value="duration")int duration,
			@PathParam(value="result")boolean result,
			@PathParam(value="idPath")int idPath){
		Parcours p = em.find(Parcours.class	, idPath);
		Treatment t = new Treatment();
		t.setParcours(p);
		t.setDescription(description);
		t.setDuration(duration);
		t.setResult(result);
		TreatmentService.addTreatement(t);
		return Response.ok().entity("New treatment").build();
		
	}
	@DELETE
	@javax.ws.rs.Produces(MediaType.APPLICATION_XML)
	@Path("/deleteTreatment/{id}")
	public Response deleteTreatement(@PathParam(value="id")int id){
		
		TreatmentService.deleteTreatement(id);
		return Response.ok().entity("deleted treatement").build();

}
	@PUT
	@Path("/updateTreatement")
	@Consumes(MediaType.APPLICATION_XML)
	public Response editTreatment(Treatment treatement){
		TreatmentService.editTreatment(treatement);
		return  Response.ok().entity("updated treatement").build();
}
	

	@GET
	@Path("/findAllPath")
	@javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
	public List<Treatment> getAllParcours(@QueryParam(value="id")int id){
		List<Treatment> l=TreatmentService.findTrByPar(id);
		if (l.isEmpty())
		{return null;}
		else return l;
	}	
	
	
}
