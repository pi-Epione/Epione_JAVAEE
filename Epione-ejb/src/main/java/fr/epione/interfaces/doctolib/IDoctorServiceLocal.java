package fr.epione.interfaces.doctolib;

import java.util.List;

import javax.ejb.Local;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fr.epione.entity.Adresse;
import fr.epione.entity.DemandeDoctolib;
import fr.epione.entity.Doctor;

@Local
public interface IDoctorServiceLocal {

	int addDoctor(Doctor doctor) ;
	List<Doctor> getDoctors() ;
	int ajoutDemande(DemandeDoctolib demande) ; 
	Adresse ParseAdresse(String adresse)  ;
	List<DemandeDoctolib> getDemandes() ; 
}
