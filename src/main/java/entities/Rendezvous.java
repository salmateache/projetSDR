package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @NamedQuery(name = "Rendezvous.findByService", query = "SELECT r FROM Rendezvous r WHERE r.service = :service")
})
public class Rendezvous implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

      @PersistenceContext(unitName = "TonPU")  // <-- Remplace "TonPU" par le nom de ta persistence-unit dans persistence.xml
    private EntityManager em;
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

    // 🔹 Relation vers l'entité Utilisateur (associée par id_utilisateur si applicable)
    @ManyToOne(optional = true)
    @JoinColumn(name = "id_utilisateur", referencedColumnName = "id", insertable = false, updatable = false)
    private Utilisateur utilisateur;

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

    // Getters & Setters
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rendezvous)) {
            return false;
        }
        Rendezvous other = (Rendezvous) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Rendezvous[ id=" + id + " ]";
    }
    public List<Rendezvous> findConfirmedRendezvous() {
    Date now = new Date();
    TypedQuery<Rendezvous> query = em.createQuery(
        "SELECT r FROM Rendezvous r " +
        "WHERE LOWER(r.statut) = :statut " +
        "AND FUNCTION('TIMESTAMP', r.dateRdv, r.heureRdv) > :now", Rendezvous.class);
    
    query.setParameter("statut", "confirmé");
    query.setParameter("now", now);

    return query.getResultList();
}

}
