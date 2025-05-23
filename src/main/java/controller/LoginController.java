package controller;

import ejb.UtilisateurFacade;
import entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private UtilisateurFacade userEJB;

    private String email;
    private String password;
    private Utilisateur utilisateurConnecte;

    public LoginController() {}

    public String login() {
    List<Utilisateur> utilisateurs = userEJB.findAll();
    for (Utilisateur u : utilisateurs) {
        if ((u.getEmail().equalsIgnoreCase(email) || u.getNom().equalsIgnoreCase(email)) &&
             u.getPassword().equals(password)) {
            utilisateurConnecte = u;
            if ("admin".equalsIgnoreCase(u.getRole())) {
                return "admin.xhtml?faces-redirect=true";
            } else if ("patient".equalsIgnoreCase(u.getRole())) {
                return "patient.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rôle inconnu", null));
                return null;
            }
        }
    }
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Identifiants invalides", null));
    return null;
}


    // Getters & Setters
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

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }
}