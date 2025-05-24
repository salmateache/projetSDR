package ejb;

import entities.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

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
            return null;
        } catch (NonUniqueResultException e) {
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

    // Nouvelle méthode pour récupérer uniquement les utilisateurs avec le rôle "patient"
    public List<Utilisateur> findPatients() {
        return em.createQuery("SELECT u FROM Utilisateur u WHERE u.role = :role", Utilisateur.class)
                 .setParameter("role", "patient")
                 .getResultList();
    }
}
