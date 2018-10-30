package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Remote;

import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;

@Remote
public interface IparcoursServiceRemote {
	void addPath(Parcours parcours);
	void deletePath(int id);
	void editPath(Parcours parcours);
	List<Parcours> findAllPaths();
	public  List<Treatment> affecterTreatParcours(int treatId, int pathId);
	 List<Parcours> findParByUser(int id);
}
