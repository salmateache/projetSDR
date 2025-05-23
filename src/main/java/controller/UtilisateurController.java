/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package controller;

import ejb.UtilisateurFacade;
import entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author USER
 */
@Named(value = "utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {
@EJB
    private UtilisateurFacade userEJB;

    /**
     * Creates a new instance of UtilisateurController
     */
    public UtilisateurController() {
    }
    public List<Utilisateur> findAll(){
        return this.userEJB.findAll();
    }
}
