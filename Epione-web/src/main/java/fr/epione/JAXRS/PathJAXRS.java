package fr.epione.JAXRS;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
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

import fr.epione.entity.Doctor;
import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;
import fr.epione.interfaces.IparcoursServiceLocal;
import fr.epione.interfaces.IuserServiceLocal;
@Path("Path")
@RequestScoped
public class PathJAXRS {
	
	@PersistenceContext
	EntityManager em;
	@Inject
	IparcoursServiceLocal ParcoursService;	
	
	@POST
	@javax.ws.rs.Produces(MediaType.APPLICATION_XML)
	@Path("/addPath/{justification}/{isState}/{doctorNote}/{idDoctor}")
	public Response addPath (@PathParam(value="justification")String justification
			,@PathParam(value="isState")boolean isState,@PathParam(value="doctorNote")String doctorNote
			,@PathParam(value="idDoctor")int idDoctor){
		Doctor d = em.find(Doctor.class	, idDoctor);
		Parcours p = new Parcours();
		p.setDoctor(d);
		p.setDoctorNote(doctorNote);
		p.setJustification(justification);
		p.setState(isState);
		ParcoursService.addPath(p);
		return Response.ok().entity("New path").build();
		
	}
	
	@DELETE
	@javax.ws.rs.Produces(MediaType.APPLICATION_XML)
	@Path("/deletPath/{id}")
	public Response deletePath(@PathParam(value="id")int id){
		
		ParcoursService.deletePath(id);
		return Response.ok().entity("deleted path").build();
		
	}
	
	@PUT
	@Path("/updatePath")
	@Consumes(MediaType.APPLICATION_XML)
	public Response editPath(Parcours parcours){
		ParcoursService.editPath(parcours);
		return  Response.ok().entity("updated path").build();
		
	}
	
	@GET
	@Path("/findAllPaths")
	@javax.ws.rs.Produces(MediaType.APPLICATION_XML)
	public List<Parcours> getAllParcours(){
		return ParcoursService.findAllPaths();
	}
	
   
	@GET
	@Path("/UserPath")
	@javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
	public Parcours getAllParcours(@QueryParam(value="id")int id){
		List<Parcours> l=ParcoursService.findParByUser(id);
		if (l.isEmpty())
		{return null;}
		else return l.get(0);
	}
	
	
	
	
	
	
	

}