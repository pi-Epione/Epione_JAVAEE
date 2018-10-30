package fr.epione.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement

public class Parcours implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String doctorNote;
	private boolean state;
	private String justification;
	
	
	/* association parcours patient*/
	@OneToOne(mappedBy="parcours")
	private Patient patient;
	/* association parcours Rdv*/
	@OneToMany(mappedBy = "parcours")
	private List<RendezVous> rendezVous= new ArrayList<>(); ;
	// association parcours docteur
	@ManyToOne()
	private Doctor doctor;
	//association traitement parcours
	@OneToMany(mappedBy = "parcours",cascade=CascadeType.PERSIST)
	private List<Treatment> treatment= new ArrayList<>();;
	
	@XmlElement(name="DoctorNote")
	public String getDoctorNote() {
		return doctorNote;
	}
	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}
	@XmlElement(name="isState")
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	@XmlElement(name="justification")
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	@XmlAttribute(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Parcours(int id, String doctorNote, boolean state, String justification, Patient patient,
			List<RendezVous> rendezVous, Doctor doctor, List<Treatment> treatment) {
		super();
		this.id = id;
		this.doctorNote = doctorNote;
		this.state = state;
		this.justification = justification;
		this.patient = patient;
		this.rendezVous = rendezVous;
		this.doctor = doctor;
		this.treatment = treatment;
	}
	public Parcours() {
		
	}
	
	
}
