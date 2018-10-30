package fr.epione.interfaces;

import java.util.List;

import fr.epione.entity.Treatment;

public interface ItreatementServiceRemote {
	void addTreatement(Treatment treatement);
	List<Treatment> afficheTrait(int id);
	List<Treatment> findTrByPar(int id);
	
}
