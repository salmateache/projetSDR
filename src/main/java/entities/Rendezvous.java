/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "rendezvous")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rendezvous.findAll", query = "SELECT r FROM Rendezvous r"),
    @NamedQuery(name = "Rendezvous.findById", query = "SELECT r FROM Rendezvous r WHERE r.id = :id"),
    @NamedQuery(name = "Rendezvous.findByDateRdv", query = "SELECT r FROM Rendezvous r WHERE r.dateRdv = :dateRdv"),
    @NamedQuery(name = "Rendezvous.findByHeureRdv", query = "SELECT r FROM Rendezvous r WHERE r.heureRdv = :heureRdv"),
    @NamedQuery(name = "Rendezvous.findByStatut", query = "SELECT r FROM Rendezvous r WHERE r.statut = :statut"),
    @NamedQuery(name = "Rendezvous.findByIdUtilisateur", query = "SELECT r FROM Rendezvous r WHERE r.idUtilisateur = :idUtilisateur"),
    @NamedQuery(name = "Rendezvous.findByService", query = "SELECT r FROM Rendezvous r WHERE r.service = :service")})
public class Rendezvous implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_rdv")
    @Temporal(TemporalType.DATE)
    private Date dateRdv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heure_rdv")
    @Temporal(TemporalType.TIME)
    private Date heureRdv;
    @Size(max = 10)
    @Column(name = "statut")
    private String statut;
    @Lob
    @Size(max = 65535)
    @Column(name = "commentaire")
    private String commentaire;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_utilisateur")
    private long idUtilisateur;
    @Size(max = 100)
    @Column(name = "service")
    private String service;

    public Rendezvous() {
    }

    public Rendezvous(Integer id) {
        this.id = id;
    }

    public Rendezvous(Integer id, Date dateRdv, Date heureRdv, long idUtilisateur) {
        this.id = id;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(Date dateRdv) {
        this.dateRdv = dateRdv;
    }

    public Date getHeureRdv() {
        return heureRdv;
    }

    public void setHeureRdv(Date heureRdv) {
        this.heureRdv = heureRdv;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rendezvous)) {
            return false;
        }
        Rendezvous other = (Rendezvous) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rendezvous[ id=" + id + " ]";
    }
    
}
