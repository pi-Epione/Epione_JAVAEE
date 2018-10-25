package fr.epione.JAXRS;

import java.util.List;

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

import fr.epione.entity.Doctor;
import fr.epione.interfaces.doctolib.IDoctorServiceLocal;



@Path("doctolib")
@RequestScoped
public class DoctolibJAXRS {

	@Inject
	IDoctorServiceLocal DS;
	
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
		List<Doctor> list = DS.getDoctors();
		GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(list){};
		return Response.ok(entity).build();
	}
	
	
	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test(){
		List<Doctor> list = DS.getDoctors();
		GenericEntity<List<Doctor>> entity = new GenericEntity<List<Doctor>>(list){};
		return "hello";
	}
}
