package fr.epione.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RendezVous implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date date ; 
	private int heureDebut ; 
	private int heureFin ; 
	private boolean etat ;
	private String reason;
	@Enumerated(EnumType.STRING)
	private State state;
	private int order;
	
	@ManyToOne
	private Patient patient ;
	
	private MotifDoctor motif ; 
	private Doctor Medecin ; 
	
	public RendezVous() {
		super();
	} 
	public RendezVous(Date date, int heureDebut, int heureFin, boolean etat, Patient patient) {
		super();
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.etat = etat;
		this.patient = patient;
	}
	public int getIdentifiant() {
		return id;
	}
	public void setIdentifiant(int identifiant) {
		this.id = identifiant;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getHeureDebut() {
		return heureDebut;
	}
	public void setHeureDebut(int heureDebut) {
		this.heureDebut = heureDebut;
	}
	public int getHeureFin() {
		return heureFin;
	}
	public void setHeureFin(int heureFin) {
		this.heureFin = heureFin;
	}
	public boolean isEtat() {
		return etat;
	}
	public void setEtat(boolean etat) {
		this.etat = etat;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public MotifDoctor getMotif() {
		return motif;
	}
	public void setMotif(MotifDoctor motif) {
		this.motif = motif;
	}
	public Doctor getMedecin() {
		return Medecin;
	}
	public void setMedecin(Doctor medecin) {
		Medecin = medecin;
	}
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public String toString() {
		return "RendezVous [date=" + date + ", heureDebut=" + heureDebut + ", heureFin=" + heureFin + ", etat=" + etat
				+ ", patient=" + patient + "]";
	}
}
