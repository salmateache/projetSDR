package controller;

import ejb.UtilisateurFacade;
import entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named(value = "utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UtilisateurFacade userEJB;

    private Utilisateur utilisateurConnecte;
    private String email;
    private String password;

    // Nouvel attribut pour stocker le patient sélectionné
    private Utilisateur selectedPatient;

    public UtilisateurController() {
    }

    public List<Utilisateur> findAll() {
        return this.userEJB.findAll();
    }

    public List<Utilisateur> getPatients() {
        return this.userEJB.findPatients();
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Utilisateur getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Utilisateur selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    // Méthode pour charger les détails d'un patient via son ID
    public void loadPatientDetails(Long patientId) {
        selectedPatient = userEJB.find(patientId);
        if (selectedPatient == null || !"PATIENT".equalsIgnoreCase(selectedPatient.getRole())) {
            // patient non trouvé ou rôle incorrect
            selectedPatient = null;
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Patient non trouvé ou accès interdit"));
        }
    }

    // Méthode de vérification de connexion pour sécuriser les pages
    public void verifierConnexion(ComponentSystemEvent event) {
        if (utilisateurConnecte == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            try {
                // Rediriger vers login.xhtml si pas connecté
                context.getExternalContext().redirect("login.xhtml");
                context.responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String login() {
        utilisateurConnecte = userEJB.findByEmailAndPassword(email, password);
        if (utilisateurConnecte != null) {
            return "dashboard.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Email ou mot de passe incorrect"));
            return null;
        }
    }

    public String logout() {
        utilisateurConnecte = null;
        email = null;
        password = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml?faces-redirect=true";
    }

    public String updateProfil() {
        try {
            userEJB.edit(utilisateurConnecte);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Profil mis à jour avec succès"));
            return "manageProfil.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Impossible de mettre à jour le profil"));
            return null;
        }
    }
}
