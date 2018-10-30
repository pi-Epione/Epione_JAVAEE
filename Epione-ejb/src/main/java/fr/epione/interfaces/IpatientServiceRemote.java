package fr.epione.interfaces;

import javax.ejb.Remote;

import fr.epione.entity.Patient;

@Remote
public interface IpatientServiceRemote {

	public int updateProfile(Patient p) ; 
}
