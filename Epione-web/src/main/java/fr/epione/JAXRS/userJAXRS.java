package fr.epione.JAXRS;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import fr.epione.entity.Doctor;
import fr.epione.entity.User;
import fr.epione.interfaces.IuserServiceLocal;

@Path("users")
@RequestScoped
public class userJAXRS {

	@Inject
	IuserServiceLocal userService;

	@Path("test")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(Doctor user){
		int id = userService.addUser(user);
		return Response.ok(id).build();
	}
	
	@POST
	@Path("signIn")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signIn(Doctor doctor) {
		return Response.ok(userService.signIn(doctor)).build();
	}

	@POST
	@Path("logIn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logIn(@QueryParam(value = "email") String email, @QueryParam(value = "password") String password) {
		return Response.ok(userService.logIn(email, password)).build();
	}

	@Path("logOut")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response logOut(@QueryParam(value = "idUser") int idUser) {
		return Response.ok(userService.logOut(idUser)).build();
	}

	@GET
	@Path("user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@QueryParam(value = "idUser") int idUser) {
		return Response.ok(userService.getUserById(idUser)).build();
	}

	@Path("enableUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserEnable() {
		List<User> list = userService.getUsersEnable();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(list){};
		return Response.ok(entity).build();
	}
	
	@Path("allUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(){
		List<User> list = userService.getAllUsers();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(list){};
		return Response.ok(entity).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response test() {

		JsonObject value2 = Json.createObjectBuilder().add("firstName", "John").add("lastName", "Smith").add("age", 25)
				.add("address",
						Json.createObjectBuilder().add("streetAddress", "21 2nd Street").add("city", "New York")
								.add("state", "NY").add("postalCode", "10021"))
				.add("phoneNumber",
						Json.createArrayBuilder()
								.add(Json.createObjectBuilder().add("type", "home").add("number", "212 555-1234"))
								.add(Json.createObjectBuilder().add("type", "fax").add("number", "646 555-4567")))
				.build();
		System.out.println(value2.getJsonArray("phoneNumber"));
		return null;
	}
}
