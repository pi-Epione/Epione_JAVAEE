package fr.epione.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Patient extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Adresse adresse;
	@OneToMany(mappedBy = "patient")
	private List<RendezVous> listeRendezVous = new ArrayList<>();

	public List<RendezVous> getListeRendezVous() {
		return listeRendezVous;
	}

	public void setListeRendezVous(List<RendezVous> listeRendezVous) {
		this.listeRendezVous = listeRendezVous;
	}

	public Patient() {
		super();
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public Patient(String nom, String prenom, String email, Adresse adresse, int telephone, Date dateDeNaissance,
			String motDePasse, List<RendezVous> listeRendezVous) {
		super(nom, prenom, email, telephone, dateDeNaissance, motDePasse);
		this.listeRendezVous = listeRendezVous;
		this.adresse = adresse;
	}

}
