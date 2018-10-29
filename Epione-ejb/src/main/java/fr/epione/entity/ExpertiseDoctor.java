package fr.epione.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExpertiseDoctor implements Serializable{

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private int id ; 
	private String nom ;
	public ExpertiseDoctor() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	@Override
	public String toString() {
		return "ExpertiseDoctor [id=" + id + ", nom=" + nom + "]";
	} 
	
	
}

