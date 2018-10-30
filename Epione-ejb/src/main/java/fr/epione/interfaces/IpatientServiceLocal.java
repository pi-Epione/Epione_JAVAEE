package fr.epione.interfaces;

import javax.ejb.Local;

import fr.epione.entity.Patient;

@Local
public interface IpatientServiceLocal {

	public int updateProfile(Patient p) ; 
}
