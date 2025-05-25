package controller;

import ejb.UtilisateurFacade;
import entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

@Named(value = "utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {
    
    @EJB
    private UtilisateurFacade userEJB;

    public UtilisateurController() {
    }

    public List<Utilisateur> findAll(){
        return this.userEJB.findAll();
    }

    // Nouvelle méthode qui retourne la liste des patients uniquement
    public List<Utilisateur> getPatients() {
        return this.userEJB.findPatients();
    }
}
