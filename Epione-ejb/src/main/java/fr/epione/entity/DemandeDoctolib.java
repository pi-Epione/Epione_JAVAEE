package fr.epione.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DemandeDoctolib {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private int id ;
	private String firstName ;
	private String lastName ;
	private String specialite; 
	private String ville ;
<<<<<<< HEAD
	private String email;
=======
>>>>>>> a9d9caf84f30ca383256ca956035502bf13b16e9
	
	
	public DemandeDoctolib() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSpecialite() {
		return specialite;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
<<<<<<< HEAD
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
=======
>>>>>>> a9d9caf84f30ca383256ca956035502bf13b16e9
	@Override
	public String toString() {
		return "DemandeDoctolib [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", specialite="
				+ specialite + ", ville=" + ville + "]";
	} 
	
	
	
	
}
