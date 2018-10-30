package fr.epione.JAXRS;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;
import fr.epione.interfaces.IrendezvousServiceLocal;
import fr.epione.interfaces.rendezvousManipSreviceLocal;

@Path("rendezvousM")

public class RendezvousManipRessources  {
	@Inject
	rendezvousManipSreviceLocal rdvsm ;
	
	@Path("note")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addnote(@QueryParam(value = "id") int id, @QueryParam(value = "note") int note){
		return Response.ok(rdvsm.noteVisite(id, note)).build();
	}

	@Path("cancel")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Patient cancelP(@QueryParam(value = "id") int id){
		return rdvsm.cancelRdvNotif(id);
}
	
	@Path("propose")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response propose(@QueryParam(value = "id") int id){
		return Response.ok(rdvsm.PropositonRdv(id)).build();
}
	@Path("accept")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response accept(@QueryParam(value = "id") int id){
		return Response.ok(rdvsm.acceptRdv(id)).build();
}
}