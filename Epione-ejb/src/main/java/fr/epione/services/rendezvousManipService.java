//package fr.epione.services;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.Resource;
//import javax.ejb.Stateless;
//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.naming.ldap.Rdn;
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//
//import fr.epione.entity.Doctor;
//import fr.epione.entity.NotePatient;
//import fr.epione.entity.Patient;
//import fr.epione.entity.RendezVous;
//import fr.epione.entity.State;
//import fr.epione.interfaces.rendezvousManipSreviceLocal;
//
//@Stateless
//public class rendezvousManipService implements rendezvousManipSreviceLocal {
//	@PersistenceContext(unitName = "epione")
//	EntityManager em;
//	rendezvousService rdc = new rendezvousService();
//	ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//
//	
//
//	
//	@Override
//	public Patient cancelRdvNotif(int rdvid) {
//		RendezVous rdv=em.find(RendezVous.class,rdvid);
//		Date dt = new Date();
//		Date lastd=rdv.getDate();
//		Calendar c = Calendar.getInstance(); 
//		c.setTime(dt); 
//		c.add(Calendar.DATE, 2);
//		dt = c.getTime();
//		Calendar a = Calendar.getInstance(); 
//		Calendar b = Calendar.getInstance(); 
//		a.setTime(rdv.getDate());
//		
//		if(rdv.getDate().before(dt))
//		{
//		
//		Doctor d =em.find(Doctor.class, rdv.getMedecin().getId());
//		TypedQuery<RendezVous> query1 = em.createQuery(
//				"select r from RendezVous r where r.Medecin = :d", RendezVous.class).setParameter("d", d) ;
//		List<RendezVous> l = query1.getResultList();
//		for(RendezVous r : l){
//			
//			b.setTime(lastd);
//			a.setTime(rdv.getDate());
//			if ((r.getDate().after(lastd))&&((a.get(Calendar.DAY_OF_YEAR))==(b.get(Calendar.DAY_OF_YEAR))))
//					
//				lastd=r.getDate();
//			
//				
//			
//		}
//		TypedQuery<RendezVous> query = em.createQuery(
//				"select r from RendezVous r where r.Medecin = :d and r.date=:dt ", RendezVous.class).setParameter("d", d).setParameter("dt", lastd) ;
//	 Patient lastP = query.getSingleResult().getPatient();
//	
//		return lastP;
//	
//		
//		}
//		
//		return null;
//		
//		}
//	
//	
//	
//	@Override
//	public  JsonObject PropositonRdv(int rdvid) {
//		RendezVous rdv=em.find(RendezVous.class,rdvid);
//		Patient p = cancelRdvNotif(rdvid);
//		if(p!=null){
//			p=em.find(Patient.class, p.getId());
//			rdv.setPatient(p);
//			rdv.setReason(null);
//			rdv.setState(State.waiting);
//           /*
//            new java.util.Timer().schedule( 
//                    new java.util.TimerTask() {
//                        @Override
//                        public void run() {
//                            if(rdv.getState()==State.waiting){
//                            	Doctor d =em.find(Doctor.class, rdv.getMedecin().getId());
//                            	TypedQuery<RendezVous> query = em.createQuery(
//                        				"select r from RendezVous r where r.doctor = :d and r.date=:dt ", RendezVous.class).setParameter("d", d).setParameter("dt", lastd) ;
//                            	
//                        }}
//                    }, 
//                    5000 
//            );*/
//			return Json.createObjectBuilder().add("success", "rendez vous propopse").build();
//			
//		}
//	return	rdc.cancelRendezVous(rdv.getIdentifiant());
//		
//		
//	}
//
//	@Override
//	public  JsonObject noteVisite(int rdvid,int note) {
//		try{
//		Date today = new Date();
//		NotePatient n = new NotePatient();
//		RendezVous rdv=em.find(RendezVous.class,rdvid);
//		//if(rdv.getDate().before(today))
//			n.setNote(note);
//		n.setDoctor(rdv.getMedecin());
//		n.setPatient(rdv.getPatient());
//		em.persist(n);
//		return Json.createObjectBuilder().add("success", "note a été ajouté").build();
//		} catch (NoResultException e) {
//			return Json.createObjectBuilder().add("error", "une erreur est survenue").build();
//		}
//		
//	}
//	@Override
//	public  JsonObject acceptRdv(int p){
//	
//	Patient P =em.find(Patient.class, p);
//	
//	TypedQuery<RendezVous> query = em.createQuery(
//			"select r from RendezVous r where r.patient = :p", RendezVous.class).setParameter("p", P);
// List<RendezVous> rdvp = query.getResultList(); 
// for(RendezVous r : rdvp)
// {
//	 if (r.getState()==State.waiting)
//		 r.setState(State.accepted);
//	 else
//		 em.remove(r);
//		
// }
// return Json.createObjectBuilder().add("sucess", "rdv accepté").build();
//	
//	}
//		
//	}
//	
