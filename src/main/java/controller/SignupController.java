package controller;
import ejb.UtilisateurFacade;
import entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Named
@SessionScoped
public class SignupController implements Serializable {
    
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private String telephone;
    
    

 @EJB
    private UtilisateurFacade userEJB;

public String register() {
    if (nom == null || prenom == null || email == null || password == null || role == null || telephone == null ||
        nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty() || telephone.isEmpty()) {

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tous les champs sont obligatoires", null));
        return null;
    }

    if (!password.equals(confirmPassword)) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas.", null));
        return null; // Reste sur la page
    }

    // Vérification de l'existence de l'utilisateur
    Utilisateur existingUser = userEJB.findByEmail(email);
    if (existingUser != null) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Un compte avec cet email existe déjà.", null));
        return null;
    }

    // Hashage du mot de passe
    String hashedPassword = hashPassword(password);

    Utilisateur u = new Utilisateur();
    u.setNom(nom);
    u.setPrenom(prenom);
    u.setEmail(email);
    u.setPassword(hashedPassword);  // <-- mot de passe hashé
    u.setRole(role);
    u.setTelephone(telephone);

    userEJB.create(u);

    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Compte créé avec succès !", null));

    return "login.xhtml?faces-redirect=true";
}

// Méthode utilitaire pour hasher le mot de passe avec SHA-256
private String hashPassword(String password) {
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
    public String goToSignup() {
    return "signup.xhtml?faces-redirect=true";
}


    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() {  return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom;  }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getTelephone() {  return telephone;  }
    public void setTelephone(String telephone) { this.telephone = telephone;  }
    public String getConfirmPassword() { return confirmPassword;}
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword;}
  
}