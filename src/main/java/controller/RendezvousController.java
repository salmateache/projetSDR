package controller;

import entities.Rendezvous;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import ejb.RendezvousFacade;

@Named("rendezvousController")
@SessionScoped
public class RendezvousController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RendezvousFacade rendezvousFacade;

    private List<Rendezvous> rendezvousList;
    private Rendezvous selectedRdv;

    public RendezvousController() {
        // Constructeur vide
    }

    /** 
     * Retourne la liste des rendez-vous depuis la base.
     */
    public List<Rendezvous> getRendezvousList() {
        if (rendezvousList == null) {
            rendezvousList = rendezvousFacade.findAll();
        }
        return rendezvousList;
    }

    /**
     * Prépare le formulaire de modification pour le report.
     */
   public String prepareReport(Rendezvous rdv) {
    this.selectedRdv = rdv;
    return "reporterRdv.xhtml?faces-redirect=true";
}

public String saveReport() {
    if (selectedRdv != null) {
        rendezvousFacade.edit(selectedRdv);
        rendezvousList = null; // rafraîchir la liste
        return "listerendezvous.xhtml?faces-redirect=true";
    }
    return null;
}

    // Getters et setters

    public Rendezvous getSelectedRdv() {
        return selectedRdv;
    }

    public void setSelectedRdv(Rendezvous selectedRdv) {
        this.selectedRdv = selectedRdv;
    }
    
    /**
 * Retourne le nom complet (prénom + nom) de l'utilisateur lié à un rendez-vous.
 * @param rdv Le rendez-vous
 * @return Le nom complet de l'utilisateur ou une chaîne vide si utilisateur null
 */
public String getNomCompletUtilisateur(Rendezvous rdv) {
    if (rdv != null && rdv.getUtilisateur() != null) {
        return rdv.getUtilisateur().getPrenom() + " " + rdv.getUtilisateur().getNom();
    }
    return "";
}
}
