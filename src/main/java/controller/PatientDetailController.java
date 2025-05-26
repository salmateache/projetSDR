package controller;
import ejb.RendezvousFacade;
import ejb.UtilisateurFacade;
import entities.Rendezvous;
import entities.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.Column;
import java.util.Map;

@Named
@ViewScoped
public class PatientDetailController implements Serializable {
    private Utilisateur selectedPatient;
    private List<Rendezvous> rendezvousList;
    private int patientId;
   

@Column(name = "id_utilisateur")
private Integer idUtilisateur;

public Integer getIdUtilisateur() {
    return idUtilisateur;
}

public void setIdUtilisateur(Integer idUtilisateur) {
    this.idUtilisateur = idUtilisateur;
}
    @EJB
    private UtilisateurFacade utilisateurFacade;

    @EJB
    private RendezvousFacade rendezvousFacade;

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                
                patientId = Integer.parseInt(idParam);
                selectedPatient = utilisateurFacade.find(patientId);
                rendezvousList = rendezvousFacade.findByUtilisateurId(patientId);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Gérer proprement en prod
            }
        }
    }

    public Utilisateur getSelectedPatient() {
        return selectedPatient;
    }

    public List<Rendezvous> getRendezvousList() {
        return rendezvousList;
    }
}