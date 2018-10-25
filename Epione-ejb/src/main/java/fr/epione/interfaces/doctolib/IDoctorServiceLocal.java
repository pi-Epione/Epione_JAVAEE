package fr.epione.interfaces.doctolib;

import java.util.List;

import fr.epione.entity.Doctor;

public interface IDoctorServiceLocal {

	int addDoctor(Doctor doctor) ;
	List<Doctor> getDoctors() ;
}
