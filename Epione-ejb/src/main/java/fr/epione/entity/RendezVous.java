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

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class RendezVous implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id; 
	@Temporal(TemporalType.TIMESTAMP)
	private Date date ; 
	private String reason;
	@Enumerated(EnumType.STRING)
	private State state;
	
	

	
	@ManyToOne
	private Patient patient ;
	@ManyToOne
	private MotifDoctor motif ; 
	@ManyToOne
	private Doctor Medecin ; 
	
	public RendezVous() {
		super();
	} 
	public RendezVous(Date date, Patient patient) {
		super();
		this.date = date;
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
	
	@JsonIgnore
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	@JsonIgnore
	public MotifDoctor getMotif() {
		return motif;
	}
	public void setMotif(MotifDoctor motif) {
		this.motif = motif;
	}
	@JsonIgnore
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
	
}
