/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejb;

import entities.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }
    
    
    
    public Utilisateur findByEmailAndPassword(String email, String password) {
    try {
        return em.createNamedQuery("Utilisateur.findByEmailAndPassword", Utilisateur.class)
                 .setParameter("email", email)
                 .setParameter("password", password)
                 .getSingleResult();
    } catch (NoResultException e) {
        return null; // Aucun utilisateur trouvé avec cet email et mot de passe
    } catch (NonUniqueResultException e) {
        // Plus d'un utilisateur trouvé, ce qui ne devrait pas arriver
        e.printStackTrace();
        return null;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
 
    public Utilisateur findByEmail(String email) {
    try {
        return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                 .setParameter("email", email)
                 .getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
}

    
    

    
}
