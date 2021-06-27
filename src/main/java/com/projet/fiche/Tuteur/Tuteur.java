package com.projet.fiche.Tuteur;

public class Tuteur {
    private int idTuteur;
    private String nom;
    private String prenom;
    private String fonction;
    private String service;
    private int numTelephone;
    private String mail;
    private String adresse;
    private String disponibilite;

    Tuteur(){}

     public int getId(){
        return this.idTuteur;
    }

    public void setId(int idTuteur){
        this.idTuteur=idTuteur;
    }

    public String getNom(){
        return this.nom;
    }

    public void setNom(String nom){
        this.nom=nom;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public void setPrenom(String prenom){
        this.prenom=prenom;
    }

    public String getFonction(){
        return this.fonction;
    }

    public void setFonction(String fonction){
        this.fonction=fonction;
    }

    public String getService(){
        return this.service;
    }

    public void setService(String service){
        this.service=service;
    }

    public int getNumeroTel(){
        return this.numTelephone;
    }

    public void setNumeroTel(int numTelephone){
        this.numTelephone=numTelephone;
    }

    public String getMail(){
        return this.mail;
    }

    public void setMail(String mail){
        this.mail=mail;
    }

    public String getAdresse(){
        return this.adresse;
    }

    public void setAdresse(String adresse){
        this.adresse=adresse;
    }

    public String getDisponibilite(){
        return this.disponibilite;
    }

    public void setDisponibilite(String disponibilite){
        this.disponibilite= disponibilite;
    }
}
