package fr.epione.interfaces.doctolib;

import java.util.List;

import javax.ejb.Local;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fr.epione.entity.Doctor;

@Local
public interface IDoctorServiceLocal {

	int addDoctor(Doctor doctor) ;
	List<Doctor> getDoctors() ;
}
