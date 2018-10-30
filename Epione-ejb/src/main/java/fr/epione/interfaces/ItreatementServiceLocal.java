package fr.epione.interfaces;

import java.util.List;

import javax.ejb.Local;

import fr.epione.entity.Treatment;
@Local
public interface ItreatementServiceLocal {

	void addTreatement(Treatment treatement);

	void deleteTreatement(int id);

	void editTreatment(Treatment treatment);
	Treatment findById(int i);
	List<Treatment> findTrByPar(int id);

	


}
