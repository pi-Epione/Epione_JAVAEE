package fr.epione.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.Doctor;
import fr.epione.entity.Parcours;
import fr.epione.entity.Patient;
import fr.epione.entity.Treatment;
import fr.epione.interfaces.IanalyticsDoctorLocal;

@Stateless
public class analyticsDoctorService implements IanalyticsDoctorLocal{

	@PersistenceContext(unitName="epione")
	EntityManager em;
	@Override
	public int nbrPatientTraite(int idDoctor) {
		int i=0;
		Doctor doctor = em.find(Doctor.class, idDoctor);
		List<Patient> listPatient = em.createQuery("select p From Patient p JOIN p.parcours pa where pa.doctor =:doctor",Patient.class)
				.setParameter("doctor", doctor)
				.getResultList();
		List<Treatment> listTreatment = em.createQuery("select t from Treatment t",Treatment.class)
				.getResultList();
		for(Patient patient : listPatient){
			Parcours parcour = patient.getParcours();
			for(Treatment treatment : listTreatment){
				if(treatment.getParcours() == parcour){
					i++;
				}
				break;
			}
		}
		return i;
	}

}
