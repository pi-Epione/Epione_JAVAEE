package fr.epione.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Motif")
public class MotifDoctor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String type;
	private String description;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "motif")
	private List<RendezVous> listeRendezVous = new ArrayList<>();;
	
	public List<RendezVous> getListeRendezVous() {
		return listeRendezVous;
	}
	public void setListeRendezVous(List<RendezVous> listeRendezVous) {
		this.listeRendezVous = listeRendezVous;
	}
	public MotifDoctor() {
		
	}
	public MotifDoctor(String type) {
		super();
		this.type = type;
	}
	public MotifDoctor(String type, String description) {
		super();
		this.type = type;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
