package fr.epione.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Adresse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String rue;
	private String ville;
	private String codePostal;
	private String numAppart;

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getNumAppart() {
		return numAppart;
	}

	public void setNumAppart(String numAppart) {
		this.numAppart = numAppart;
	}

}
