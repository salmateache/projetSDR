package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RENDEZVOUS")
public class Rendezvous implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_rdv")
    private Date dateRdv;

    @Temporal(TemporalType.TIME)
    @Column(name = "heure_rdv")
    private Date heureRdv;

    @Column(name = "statut")
    private String statut;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "service")
    private String service;  // <-- changé en String

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDateRdv() { return dateRdv; }
    public void setDateRdv(Date dateRdv) { this.dateRdv = dateRdv; }

    public Date getHeureRdv() { return heureRdv; }
    public void setHeureRdv(Date heureRdv) { this.heureRdv = heureRdv; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}
