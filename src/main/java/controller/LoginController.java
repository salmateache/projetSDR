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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private UtilisateurFacade userEJB;

    private String email;
    private String password;
    private Utilisateur utilisateurConnecte;
    private boolean loggedIn = false;


    public LoginController() {}

    public String login() {
    List<Utilisateur> utilisateurs = userEJB.findAll();
    String hashedInputPassword = hashPassword(password); // hash du mdp saisi

    for (Utilisateur u : utilisateurs) {
        if ((u.getEmail().equalsIgnoreCase(email) || u.getNom().equalsIgnoreCase(email)) &&
             u.getPassword().equals(hashedInputPassword)) {  // compare le hash
            utilisateurConnecte = u;
            loggedIn = true;
            if ("admin".equalsIgnoreCase(u.getRole())) {
                return "admin.xhtml?faces-redirect=true";
            } else if ("patient".equalsIgnoreCase(u.getRole())) {
                return "home.xhtml?faces-redirect=true";
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

    public String goToAppointment() {
        if (loggedIn) {
            return "appointment.xhtml?faces-redirect=true";
        } else {
            return "login.xhtml?faces-redirect=true";
        }
    }
    public String logout() {
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "login.xhtml?faces-redirect=true";
}
    public boolean isAdmin() {
    if (utilisateurConnecte != null) {
        return utilisateurConnecte.getRole() != null && utilisateurConnecte.getRole().equalsIgnoreCase("admin");
    }
    return false;
}

    
    public boolean isLoggedIn() {
        return loggedIn;
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
    public static String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
    }
}
}