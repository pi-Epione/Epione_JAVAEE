package fr.epione.services;


import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.epione.entity.Parcours;
import fr.epione.entity.Treatment;
import fr.epione.interfaces.ItreatementServiceLocal;
import fr.epione.interfaces.ItreatementServiceRemote;

@Stateless
public  class TreatementService implements ItreatementServiceLocal , ItreatementServiceRemote {
	@PersistenceContext(unitName = "epione")
	EntityManager em;


	public TreatementService() {
		super();
	}
	@Override
	public void addTreatement(Treatment treatement) {
		em.persist(treatement);
		System.out.println("treatement added successfuly");
	}
	@Override
	public void deleteTreatement(int id) {
		Treatment t= em.find(Treatment.class, id);
		em.remove(t);
		System.out.println("Treatement  successfuly deleted");
	}
	
	@Override
	public void editTreatment(Treatment treatment){
		em.merge(treatment);
		//em.flush();
		System.out.println("Treatement updated successfuly");	
	}
	@Override
	public List<Treatment> afficheTrait(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Treatment findById(int i) {
		Treatment t= em.find(Treatment.class, i);
		return t;
	}
	@Override
	public List<Treatment> findTrByPar(int id) {
		
		
List<Treatment> f= em.createQuery("SELECT f FROM Treatment f WHERE f.parcours.id =:id").setParameter("id",id).getResultList();
	
if (f.isEmpty()){
	return null;
}
else{
	return f;
}
	}

}
