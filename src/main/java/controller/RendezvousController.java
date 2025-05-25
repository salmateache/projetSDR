package controller;

import ejb.RendezvousFacade;
import ejb.UtilisateurFacade;
import entities.Rendezvous;
import entities.Utilisateur;
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

@Named(value = "rendezvousController")
@SessionScoped
public class RendezvousController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String selectedService;
    private String name;
    private String email;
    private String appointmentDate; // format "yyyy-MM-dd"
    private String appointmentTime; // format "HH:mm"

    private List<Rendezvous> rendezvousList;
    private Rendezvous selectedRdv;

    // ✅ CORRECT: inject RendezvousFacade via @EJB
    @EJB
    private RendezvousFacade rendezvousFacade;

    @EJB
    private UtilisateurFacade utilisateurFacade;

    @Inject
    private LoginController loginController;

    public RendezvousController() {}

    public List<Rendezvous> getRendezvousList() {
        if (rendezvousList == null) {
            rendezvousList = rendezvousFacade.findAll();
        }
        return rendezvousList;
    }

    public List<Rendezvous> getAllRendezvous() {
        return rendezvousFacade.findAll();
    }

    public void makeAppointment() {
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            Date date = sdfDate.parse(appointmentDate);
            Date time = sdfTime.parse(appointmentTime);

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
                rdv.setIdUtilisateur(0);
            }

            if (name != null && !name.isEmpty()) {
                rdv.setCommentaire("Nom: " + name + ", Email: " + email);
            }

            rendezvousFacade.create(rdv);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Appointment successfully booked!", null));

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

    public String prepareReport(Rendezvous rdv) {
        this.selectedRdv = rdv;
        return "reporterRdv.xhtml?faces-redirect=true";
    }

    public String saveReport() {
        if (selectedRdv != null) {
            try {
                rendezvousFacade.edit(selectedRdv);
                rendezvousList = null; // pour rafraîchir la liste
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Rendez-vous modifié avec succès", null));
                return "listerendezvous.xhtml?faces-redirect=true";
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors de la modification du rendez-vous", null));
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getNomCompletUtilisateur(Rendezvous rdv) {
        if (rdv != null && rdv.getIdUtilisateur() > 0) {
            Utilisateur utilisateur = utilisateurFacade.find((int) rdv.getIdUtilisateur()); // conversion Long → Integer
            if (utilisateur != null) {
                return utilisateur.getPrenom() + " " + utilisateur.getNom();
            }
        }
        return "";
    }

    // Getters & Setters

    public Rendezvous getSelectedRdv() {
        return selectedRdv;
    }

    public void setSelectedRdv(Rendezvous selectedRdv) {
        this.selectedRdv = selectedRdv;
    }

    public String getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
