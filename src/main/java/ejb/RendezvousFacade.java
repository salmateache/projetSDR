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
    
}
