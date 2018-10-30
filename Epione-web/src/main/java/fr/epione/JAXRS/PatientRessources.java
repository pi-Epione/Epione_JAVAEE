package fr.epione.JAXRS;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;
import fr.epione.interfaces.IpatientServiceLocal;
import fr.epione.services.patientService;

@Path("patients")
@RequestScoped
public class PatientRessources {
	
	@Inject
	IpatientServiceLocal patientService ; 
	
	/************************ done ************************/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProfile(Patient patient){
		return Response.ok(patientService.updateProfile(patient)).build();
	}

}
