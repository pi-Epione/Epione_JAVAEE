package fr.epione.JAXRS;

import java.util.Date;
import java.util.List;


import javax.decorator.Delegate;
import javax.ejb.PostActivate;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.entity.Doctor;
import fr.epione.entity.MotifDoctor;
import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;
import fr.epione.entity.User;
import fr.epione.interfaces.IrendezvousServiceLocal;
import fr.epione.interfaces.IrendezvousServiceRemote;

@Path("rendezvous")
public class RendezVousRessources {
	
	@Inject
	IrendezvousServiceLocal rendezvousService ; 
	
	 /************************ done ************************/
	@Path("rendezvous")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRendezVous(RendezVous rdv){
		return Response.ok(rendezvousService.addRendezVous(rdv)).build();
	}
	
	 /************************ done ************************/
	@Path("motif")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMotifDoctor(MotifDoctor motif , @QueryParam(value = "rdvId") int rdvId){
		return Response.ok(rendezvousService.addMotifDoctor(motif, rdvId)).build();
	}
	
	 /************************ done ************************/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRendezVous(RendezVous rdv){
		int id = rendezvousService.updateRendezVous(rdv) ; 
		return Response.ok(id).build() ;
	}
	
	/************************ done ************************/
	@Path("rendezvous")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelRendezvous( @QueryParam(value = "rendezvousId") int rendezvousId){
		return Response.ok(rendezvousService.cancelRendezVous(rendezvousId)).build();
	}
	
	/************************ done ************************/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRendezVous(@QueryParam(value="doctorId") int doctorId, @QueryParam(value="patientId") int patientId, @QueryParam(value="motifId") int motifId){
		if(doctorId!=0){
			List<RendezVous> list = rendezvousService.getListRendezVousByDoctor(doctorId) ; 
			GenericEntity<List<RendezVous>> entity = new GenericEntity<List<RendezVous>>(list){};
			return Response.ok(entity).build(); 
		}
		else if(patientId!=0){
			List<RendezVous> list = rendezvousService.getListRendezVousByPatient(patientId) ; 
			GenericEntity<List<RendezVous>> entity = new GenericEntity<List<RendezVous>>(list){};
			return Response.ok(entity).build(); 
		}else if(motifId!=0){
			List<RendezVous> list = rendezvousService.getListRendezVousByMotif(motifId) ; 
			GenericEntity<List<RendezVous>> entity = new GenericEntity<List<RendezVous>>(list){};
			return Response.ok(entity).build(); 
		}
		else {
			List<RendezVous> list = rendezvousService.getListRendezVous() ; 
		GenericEntity<List<RendezVous>> entity = new GenericEntity<List<RendezVous>>(list){};
		return Response.ok(entity).build();}
	}
	
	/************************ done ************************/
	@Path("confirmation")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response confimRendezVous(@QueryParam(value = "rendezvouId") int rendezvousId){
		int id = rendezvousService.confimRendezVous(rendezvousId) ; 
		return Response.ok(id).build() ;
	}
	
	/************************ done ************************/
	@Path("state")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStateRendezVous(@QueryParam(value = "doctorId") int doctorId){
		List<RendezVous> list = rendezvousService.getStateRendezVous(doctorId) ; 
		GenericEntity<List<RendezVous>> entity = new GenericEntity<List<RendezVous>>(list){};
		return Response.ok(entity).build();
	}
	/************************ done ************************/
	@Path("available")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDoctorAvailability(@QueryParam(value = "doctorId") int doctorId,@QueryParam(value="date") String date){
		return Response.ok(rendezvousService.getDoctorAvailability(doctorId, date)).build();
	}
	
	
	
	


}
