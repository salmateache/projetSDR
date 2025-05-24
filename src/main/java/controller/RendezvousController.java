package controller;

import ejb.RendezvousFacade;
import entities.Rendezvous;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER
 */
@Named(value = "rendezvousController")
@SessionScoped
public class RendezvousController implements Serializable {

    public RendezvousController() {
    }
    
    private String selectedService;
    private String name;
    private String email;
    private String appointmentDate;  // format attendu "yyyy-MM-dd"
    private String appointmentTime;  // format attendu "HH:mm"

    @EJB
    private RendezvousFacade rendezvousFacade;

    @Inject
    private LoginController loginController;  // injection corrigée

    public void makeAppointment() {
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            Date date = sdfDate.parse(appointmentDate);
            Date time = sdfTime.parse(appointmentTime);

            // Recherche plus efficace en base (ajouter une méthode dans RendezvousFacade)
            List<Rendezvous> existing = rendezvousFacade.findByDateTime(date, time);

            if (!existing.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Time slot unavailable", null));
                return;
            }

            Rendezvous rdv = new Rendezvous();
            rdv.setDateRdv(date);
            rdv.setHeureRdv(time);
            rdv.setService(selectedService);
            rdv.setStatut("en attente");
            rdv.setCommentaire("");

            if (loginController != null && loginController.getUtilisateurConnecte() != null) {
                rdv.setIdUtilisateur(loginController.getUtilisateurConnecte().getId());
            } else {
                rdv.setIdUtilisateur(0); // ou gérer le cas utilisateur non connecté autrement
            }

            // Optionnel : utiliser name/email si besoin (par exemple en commentaire)
            if (name != null && !name.isEmpty()) {
                rdv.setCommentaire("Nom: " + name + ", Email: " + email);
            }

            rendezvousFacade.create(rdv);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Appointment successfully booked!", null));

            // Réinitialisation des champs
            selectedService = "";
            name = "";
            email = "";
            appointmentDate = "";
            appointmentTime = "";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while booking appointment", null));
            e.printStackTrace();
        }
    }

    // Getters et setters

    public String getSelectedService() { return selectedService; }
    public void setSelectedService(String selectedService) { this.selectedService = selectedService; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
}
