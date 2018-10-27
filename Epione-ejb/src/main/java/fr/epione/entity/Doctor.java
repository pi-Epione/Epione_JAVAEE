package fr.epione.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity

@Table(name = "doctor")
public class Doctor extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String specialite;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOuverture;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateSansRDV;
	@Column(columnDefinition = "TEXT")
	private String presentation;
	@Embedded
	private Adresse adresse;
	private Boolean doctolib ;


	@OneToMany(fetch=FetchType.EAGER)
	private List<MotifDoctor> listMotifs = new ArrayList<>();
	@OneToMany
	private List<LangueDoctor> langues = new ArrayList<>();
	@OneToMany
	private List<ExerciceDoctor> exercices = new ArrayList<>();
	@OneToMany
	private List<FormationDoctor> formations = new ArrayList<>();
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<TarifDoctor> tarifs = new ArrayList<>();
	@OneToOne(mappedBy="doctor")
	private Calendrier calendrier;

	// ******************** M ***********************///
	

	public Doctor() {

	}

	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public List<MotifDoctor> getListMotifs() {
		return listMotifs;
	}

	public void setListMotifs(List<MotifDoctor> listMotifs) {
		this.listMotifs = listMotifs;
	}

	public Date getDateOuverture() {
		return dateOuverture;
	}

	public void setDateOuverture(Date dateOuverture) {
		this.dateOuverture = dateOuverture;
	}

	public Date getDateSansRDV() {
		return dateSansRDV;
	}

	public void setDateSansRDV(Date dateSansRDV) {
		this.dateSansRDV = dateSansRDV;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public List<LangueDoctor> getLangues() {
		return langues;
	}

	public void setLangues(List<LangueDoctor> langues) {
		this.langues = langues;
	}

	public List<ExerciceDoctor> getExercices() {
		return exercices;
	}

	public void setExercices(List<ExerciceDoctor> exercices) {
		this.exercices = exercices;
	}

	public List<FormationDoctor> getFormations() {
		return formations;
	}

	public void setFormations(List<FormationDoctor> formations) {
		this.formations = formations;
	}

	public List<TarifDoctor> getTarifs() {
		return tarifs;
	}

	public void setTarifs(List<TarifDoctor> tarifs) {
		this.tarifs = tarifs;
	}

	public Boolean getDoctolib() {
		return doctolib;
	}

	public void setDoctolib(Boolean doctolib) {
		this.doctolib = doctolib;
	}



	
}
