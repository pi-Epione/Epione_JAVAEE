package fr.epione.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.Patient;
import fr.epione.interfaces.IpatientServiceLocal;
import fr.epione.interfaces.IpatientServiceRemote;
import fr.epione.utils.Utils;

@Stateless
public class patientService implements IpatientServiceLocal, IpatientServiceRemote{

	@PersistenceContext(unitName = "epione")
	EntityManager em ;
	
	/************************ done ************************/
	@Override
	public int updateProfile(Patient p) {
		
		Patient patient = em.find(Patient.class, p.getId()) ;
		if(p.getAdresse()!=null){patient.setAdresse(p.getAdresse());}
		if(p.getBirthDay()!=null){patient.setBirthDay(p.getBirthDay());};
		userService us = new userService() ; 
		if(p.getEmail()!=null && us.emailExist(p.getEmail()) == 0){patient.setEmail(p.getEmail());}; 
		if(p.getFirstName()!=null){patient.setFirstName(p.getFirstName());};
		if(p.getLastName()!=null){patient.setLastName(p.getLastName());};
		if(p.getPassword()!=null && Utils.passwordValidator(p.getPassword())==null){patient.setPassword(p.getPassword());} ; 
		if(p.getPhoneNumber()!=0){patient.setPhoneNumber(p.getPhoneNumber());} ; 
		return patient.getId();
	}

}
