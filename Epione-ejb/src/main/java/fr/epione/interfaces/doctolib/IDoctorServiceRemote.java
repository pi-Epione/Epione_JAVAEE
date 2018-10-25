package fr.epione.interfaces.doctolib;

import java.util.List;

import javax.ejb.Remote;

import org.jsoup.nodes.Document;

import fr.epione.entity.Doctor;

@Remote
public interface IDoctorServiceRemote {

	int addDoctor(Doctor doctor) ;
	List<Doctor> getDoctors() ;
}
