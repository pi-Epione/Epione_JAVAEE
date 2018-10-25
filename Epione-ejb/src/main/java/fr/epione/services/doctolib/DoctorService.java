package fr.epione.services.doctolib;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.Doctor;
import fr.epione.interfaces.doctolib.IDoctorServiceLocal;
import fr.epione.interfaces.doctolib.IDoctorServiceRemote;

@Stateless
public class DoctorService implements IDoctorServiceLocal,IDoctorServiceRemote {

	@PersistenceContext(unitName = "epione")
	EntityManager em;
	ScrapServices Services ;
	
	@Override
	public int addDoctor(Doctor doctor) {

		em.persist(doctor);
		return doctor.getId();
	}

	@Override
	public List<Doctor> getDoctors() {
		
		List<Doctor> liste= Services.getDoctors();
		return liste;
	}

}
