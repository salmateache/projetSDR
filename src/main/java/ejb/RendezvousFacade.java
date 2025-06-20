/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejb;

import entities.Rendezvous;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER
 */
@Stateless
public class RendezvousFacade extends AbstractFacade<Rendezvous> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RendezvousFacade() {
        super(Rendezvous.class);
    }
    public List<Rendezvous> findByDateTime(Date date, Date time) {
    TypedQuery<Rendezvous> query = em.createQuery(
        "SELECT r FROM Rendezvous r WHERE r.dateRdv = :date AND r.heureRdv = :time", Rendezvous.class);
    query.setParameter("date", date);
    query.setParameter("time", time);
    return query.getResultList();
}
    public List<Rendezvous> findByUtilisateurId(long idUtilisateur) {
    return em.createQuery("SELECT r FROM Rendezvous r WHERE r.idUtilisateur = :id", Rendezvous.class)
             .setParameter("id", idUtilisateur)
             .getResultList();
}
public List<Rendezvous> findThisWeek() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
    Date start = cal.getTime();

    cal.add(Calendar.DAY_OF_WEEK, 6);
    Date end = cal.getTime();

    return em.createQuery("SELECT r FROM Rendezvous r WHERE r.dateRdv BETWEEN :start AND :end", Rendezvous.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
}

 public List<Rendezvous> findAll() {
        return em.createQuery("SELECT r FROM Rendezvous r", Rendezvous.class).getResultList();
    }
    
}
