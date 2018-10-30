package fr.epione.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id ;  
	private String content  ;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date ; 
	private boolean seen ; 
	
	@ManyToOne
	private Patient patient ;
	@ManyToOne
	private Doctor doctor ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	@JsonIgnore
	public Patient getPatient() {
		return patient;
	}
	@JsonProperty
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	@JsonIgnore
	public Doctor getDoctor() {
		return doctor;
	}
	@JsonProperty
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Message(String content, boolean seen, Patient patient, Doctor doctor) {
		super();
		this.content = content;
		this.seen = seen;
		this.patient = patient;
		this.doctor = doctor;
	} 
	
	

}
