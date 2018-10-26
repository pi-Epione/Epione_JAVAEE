package fr.epione.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Calendrier implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// @OneToMany(fetch=FetchType.EAGER)
	@OneToMany
	private List<HorairesCalendar> listHoraires;
	@OneToOne
	private Doctor doctor;

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

	public List<HorairesCalendar> getListHoraires() {
		return listHoraires;
	}

	public void setListHoraires(List<HorairesCalendar> listHoraires) {
		this.listHoraires = listHoraires;
	}

}
