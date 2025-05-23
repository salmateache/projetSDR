package controllers;

import entities.Rendezvous;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import models.RendezvousFacade;

@Named(value = "rendezvousController")
@SessionScoped
public class RendezvousController implements Serializable {

    @EJB
    private RendezvousFacade rendezvousFacade;

    public RendezvousController() {
        // Constructeur vide
    }

    public List<Rendezvous> findAll() {
        return rendezvousFacade.findAll();
    }
}
