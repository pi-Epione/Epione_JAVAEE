package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Local;

import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;

@Local
public interface IparcoursServiceLocal {

	void addPath(Parcours parcours);
	void deletePath(int id);
	void editPath(Parcours parcours);
	List<Parcours> findAllPaths();
	Parcours findPathById(int id);
    List<Treatment> affecterTreatParcours(int treatId, int pathId);
    List<Parcours> findParByUser(int id);
    
}
