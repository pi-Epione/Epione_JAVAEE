package fr.epione.interfaces;

import javax.ejb.Local;
import javax.json.JsonObject;

import fr.epione.entity.Patient;
import fr.epione.entity.RendezVous;

@Local
public interface rendezvousManipSreviceLocal {
	public Patient cancelRdvNotif(int rdvid) ;
	public  JsonObject PropositonRdv(int rdvid) ;
	public  JsonObject noteVisite(int rdvid,int note) ;
	public  JsonObject acceptRdv(int p);
}
