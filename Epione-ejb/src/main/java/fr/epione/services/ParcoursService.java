package fr.epione.services;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

import fr.epione.entity.Doctor;
import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;
import fr.epione.interfaces.IparcoursServiceLocal;
import fr.epione.interfaces.IparcoursServiceRemote;
import fr.epione.interfaces.ItreatementServiceLocal;

@Stateless
public class ParcoursService implements IparcoursServiceLocal, IparcoursServiceRemote{
	@PersistenceContext(unitName = "epione")
	EntityManager em;
@EJB
ItreatementServiceLocal ser;
	public ParcoursService() {
		
	}

	@Override
	public void addPath(Parcours parcours) {
		em.persist(parcours);
		System.out.println("Path added successfuly");
	}
	
	@Override
	public void deletePath(int id) {
		Parcours p= em.find(Parcours.class, id);
		em.remove(p);
		System.out.println("Path  successfuly deleted");
	}
	@Override
	public void editPath(Parcours parcours){
		em.merge(parcours);
		em.flush();
		System.out.println("Path updated successfuly");	
	}
	
    @Override
    public List<Parcours> findAllPaths(){
    	String requete = "SELECT p FROM Parcours p";
		return em.createQuery(requete, Parcours.class).getResultList();
    	
    	
    }
    
    @Override
    public Parcours findPathById(int id){
		return em.find(Parcours.class, id);
    	
    
    }
	/*public void affecterDocteur( int id, int doctor_id) {
		Parcours p = em.find(Parcours.class, id);
		Doctor m = em.find(Doctor.class, doctor_id);
		p.se
	
	}*/

    public  List<Treatment> affecterTreatParcours(int treatId, int pathId) {
        Treatment t = em.find(Treatment.class, treatId);
        Parcours p = em.find(Parcours.class, pathId);
        List<Treatment> lstM = new ArrayList<Treatment>();
        lstM.add(t);
        t.setParcours(p);
	return (lstM);
	
	
    }

	@Override
	public List<Parcours> findParByUser(int id) {
		List<Parcours> f= em.createQuery("SELECT f FROM Parcours f WHERE f.patient.id =:id").setParameter("id",id).getResultList();
		
		if (f.isEmpty()){
			return null;
		}
		else{
			return f;
		}
			}
	}
	
	
	
	
	
	
	
