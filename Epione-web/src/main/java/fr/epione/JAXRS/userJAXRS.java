package fr.epione.JAXRS;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.annotations.Query;

import fr.epione.entity.Device;
import fr.epione.entity.Doctor;
import fr.epione.entity.Patient;
import fr.epione.entity.User;
import fr.epione.interfaces.IuserServiceLocal;
import fr.epione.utils.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("users")
@RequestScoped
public class userJAXRS {

	@EJB
	IuserServiceLocal userService;
	@Context
	private UriInfo uriInfo;
	
	
	@Path("get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@Context HttpServletRequest req){
		return Response.ok(Utils.getIdUserFromSession(req)).build();
	}

	@Path("index")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response index(@Context HttpServletRequest req) {
		if (Utils.getIdUserFromSession(req) != 0) {
			return Response.ok("Redirection vers l'utilisateur dont ID = " + " " + Utils.getIdUserFromSession(req))
					.build();
		}
		return Response.ok("Redirection vers pasge d'accueil").build();
	}

	@POST
	@Path("signInDoctor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signInDoctor(Doctor doctor) {
		String token = issueToken();
		doctor.setToken(token);
		return Response.ok(userService.signInDoctor(doctor)).build();
	}

	@POST
	@Path("signInPatient")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signInPatien(Patient patient) {
		String token = issueToken();
		patient.setToken(token);
		return Response.ok(userService.signInPatient(patient)).build();
	}

	@POST
	@Path("logIn")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logIn(@QueryParam(value = "email") String email, @QueryParam(value = "password") String password,
			@Context HttpServletRequest req, @Context HttpServletResponse resp) {

	
		String tab[] = Utils.getOsBrowserUser(req.getHeader("User-Agent"));
		User user = userService.getUserByEmail(email);
		if (user == null) {
			return Response.ok("verifier votre email").build();
		}

		if (Utils.getIdUserFromSession(req) != 0) {
			return Response.ok("Vous etes deja connecte").build();
		}

		Device device = new Device();
		device.setIp(req.getRemoteAddr());
		device.setOs(tab[0]);
		device.setBrowser(tab[1]);
		device.setConnected(Boolean.FALSE);
		JsonObject json = userService.logIn(email, password, device);

		if (json.getInt("id") != 0) {
			Utils.createSession(req, user);
		}

		return Response.ok(json).build();

	}


	@Path("logOut")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response logOut(@Context HttpServletRequest req) {

		JsonObject json = userService.logOut(req,Utils.getIdUserFromSession(req));
		userService.logOutFromDevice(req,Utils.getIdUserFromSession(req));
		Utils.destroySession(req);

		return Response.ok(json).build();
	}

	@GET
	@Path("user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(int id) {
		return Response.ok(userService.getUserById(id)).build();
	}

	@Path("enableUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserEnable() {
		List<User> list = userService.getUsersEnable();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(list) {
		};
		return Response.ok(entity).build();

	}

	@Path("allUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		List<User> list = userService.getAllUsers();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(list) {
		};
		return Response.ok(entity).build();
	}

	@Path("deleteDoctor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDoctorById(@QueryParam(value="idDoctor") int idDoctor) {
		return Response.ok(userService.deleteDoctorById(idDoctor)).build();
	}
	
	/************************ done ************************/
	@Path("patient")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePatientById(@QueryParam(value="patientId") int patientId) {
		return Response.ok(userService.deletePatientById(patientId)).build();
	}

	@Path("token")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response tokenTest() {
		return Response.ok(issueToken()).build();
	}

	private String issueToken() {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		String keyString = "simplekey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		System.out.println("the key is : " + key.hashCode());
		System.out.println("\nuriInfo.getAbsolutePath().toString() : " + uriInfo.getAbsolutePath().toString());
		System.out.println("\nExpiration date: " + toDate(LocalDateTime.now().plusMinutes(15L)));

		String jwtToken = Jwts.builder().setIssuedAt(new Date())
				.setExpiration(toDate(LocalDateTime.now().plusMinutes(15L))).signWith(SignatureAlgorithm.HS512, key)
				.compact();
		System.out.println("the returned token is : " + jwtToken);

		return jwtToken;
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/*
	 * @GET
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public Response test() {
	 * 
	 * JsonObject value2 = Json.createObjectBuilder().add("firstName",
	 * "John").add("lastName", "Smith").add("age", 25) .add("address",
	 * Json.createObjectBuilder().add("streetAddress",
	 * "21 2nd Street").add("city", "New York") .add("state",
	 * "NY").add("postalCode", "10021")) .add("phoneNumber",
	 * Json.createArrayBuilder() .add(Json.createObjectBuilder().add("type",
	 * "home").add("number", "212 555-1234"))
	 * .add(Json.createObjectBuilder().add("type", "fax").add("number",
	 * "646 555-4567"))) .build();
	 * System.out.println(value2.getJsonArray("phoneNumber")); return null; }
	 */
}
