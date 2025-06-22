package ejb;

import entities.Rendezvous;
import entities.Utilisateur;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@Singleton
@Startup
public class SMS {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<Rendezvous> getRendezvousDuJour() {
    // Calculer demain
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.add(java.util.Calendar.DATE, 1);
    Date demain = cal.getTime();

    // Calculer après-demain pour borne supérieure
    cal.add(java.util.Calendar.DATE, 1);
    Date apresDemain = cal.getTime();

    return em.createQuery(
        "SELECT r FROM Rendezvous r WHERE r.dateRdv >= :demain AND r.dateRdv < :apresDemain", Rendezvous.class)
        .setParameter("demain", demain)
        .setParameter("apresDemain", apresDemain)
        .getResultList();
}

    private void envoyerMessageWhatsapp(String phone, String message, String API_KEY) {
        try {
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String url = "https://api.callmebot.com/whatsapp.php?phone=" +
                         phone + "&text=" + encodedMsg + "&apikey=" + API_KEY;

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);
            target.request().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void envoyerNotificationsWhatsapp() {
        List<Rendezvous> aNotifier = getRendezvousDuJour();

        for (Rendezvous rdv : aNotifier) {
            int idUtilisateur = (int) rdv.getIdUtilisateur();
            Utilisateur user = em.find(Utilisateur.class, idUtilisateur);

            if (user != null && user.getTelephone() != null && !user.getTelephone().isEmpty()) {
                if (user.getTelephone().equals("212613254954")) {
                    String key = "9651221";
                    envoyerMessageWhatsapp(user.getTelephone(), "Reminder: You have an appointment tomorrow", key);
                }
                
                
                
                
                
                
                
            }
        }
    }

    @Schedule(hour = "23", minute = "09", second = "0", persistent = false)
    public void envoyerNotificationsWhatsappChaqueJour() {
        envoyerNotificationsWhatsapp();
    }
}