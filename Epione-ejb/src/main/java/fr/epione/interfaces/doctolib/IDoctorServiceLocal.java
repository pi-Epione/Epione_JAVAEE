package fr.epione.interfaces.doctolib;

import java.util.List;

import javax.ejb.Local;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;
import fr.epione.entity.ExpertiseDoctor;
import fr.epione.entity.FormationDoctor;

@Local
public interface IDoctorServiceLocal {

	int addDoctor(Doctor doctor) ;
	List<Doctor> getDoctors() ;
	int ajoutDemande(DemandeDoctolib demande) ; 
	Adresse ParseAdresse(String adresse)  ;
	List<DemandeDoctolib> getDemandes() ; 
	void AddExpertises(List<ExpertiseDoctor> liste) ;
	void addFormations(List<FormationDoctor> liste) ; 
	void AffecterExpertise(List<ExpertiseDoctor> liste , Doctor doctor ) ;
	void affecterFormations(List<FormationDoctor> liste , Doctor doctor ) ;
	List<Doctor> getAllDoctors() ;
<<<<<<< HEAD
	int deleteDemande(DemandeDoctolib demande) ; 
	Boolean deleteDoctor(Doctor doctor);
=======
>>>>>>> a9d9caf84f30ca383256ca956035502bf13b16e9
}
