package fr.epione.JAXRS;

import java.util.List;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;


import com.notification.NotificationFactory;
import com.notification.NotificationManager;
import com.notification.NotificationFactory.Location;
import com.notification.manager.SimpleManager;
import com.notification.types.TextNotification;
import com.theme.ThemePackagePresets;
import com.utils.Time;

import javax.ws.rs.core.Response;

import fr.epione.entity.Message;
import fr.epione.interfaces.ImessageServiceLocal;

@Path("messages")
@RequestScoped
public class MessageRessources {
	
	@Inject
	ImessageServiceLocal messageService ; 
	
	/************************ done ************************/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(Message message){
		return Response.ok(messageService.sendMessage(message)).build();
	}
	
	/************************ done ************************/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessages(@QueryParam("doctorId") int doctorId, @QueryParam("patientId") int patientId, @QueryParam("messageId") int messageId){
		if(doctorId!=0){
			List<Message> list = messageService.getMessagesByDoctor(doctorId) ;  
			GenericEntity<List<Message>> entity = new GenericEntity<List<Message>>(list){};
			return Response.ok(entity).build();
		}
		else if(patientId!=0){
			List<Message> list = messageService.getMessageByPatient(patientId) ;  
			GenericEntity<List<Message>> entity = new GenericEntity<List<Message>>(list){};
			return Response.ok(entity).build();
		}
		else if(messageId!=0){
			return Response.ok(messageService.getSeenMessage(messageId)).build();
		}
		else {
			List<Message> list = messageService.getTodayMessages() ;  
			GenericEntity<List<Message>> entity = new GenericEntity<List<Message>>(list){};
			return Response.ok(entity).build();
		}
	}
	
	/************************ done ************************/
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMessage( @QueryParam(value = "messageId") int messageId){
		return Response.ok(messageService.deleteMessage(messageId)).build();
	}
	
	/************************ done ************************/
	@Path("count")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response countMessages(@QueryParam("doctorId") int doctorId, @QueryParam("patientId") int patientId){
		if(doctorId!=0){
			return Response.ok(messageService.countMessagesByDoctor(doctorId)).build();
		}
		else {
			return Response.ok(messageService.countMessagesByPatient(patientId)).build();
		}
	}
	
	
	
	
	
	
	

}
