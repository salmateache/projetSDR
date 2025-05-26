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
import java.util.List;

@Singleton
@Startup
public class SMS {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<Rendezvous> getRendezvousDuJour() {
        return em.createQuery(
                "SELECT r FROM Rendezvous r WHERE r.dateRdv = CURRENT_DATE", Rendezvous.class)
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

            System.out.println("✅ WhatsApp message sent to: " + phone);
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
                if (user.getTelephone().equals("212679539240")) {
                    String key = "1239388";
                    envoyerMessageWhatsapp(user.getTelephone(), "testttttttttttttttttttt", key);
                }
            }
        }
    }

    @Schedule(hour = "15", minute = "15", second = "0", persistent = false)
    public void envoyerNotificationsWhatsappChaqueJour() {
        envoyerNotificationsWhatsapp();
    }
}