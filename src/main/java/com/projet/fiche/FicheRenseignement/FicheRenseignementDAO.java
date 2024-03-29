package com.projet.fiche.FicheRenseignement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.projet.fiche.Etablissement.Etablissement;
import com.projet.fiche.Etablissement.EtablissementDAO;
import com.projet.fiche.Etudiant.Etudiant;
import com.projet.fiche.Etudiant.EtudiantDAO;
import com.projet.fiche.InfosStage.InfosStage;
import com.projet.fiche.InfosStage.InfosStageDAO;
import com.projet.fiche.ServiceGestion.ServiceGestion;
import com.projet.fiche.ServiceGestion.ServiceGestionDAO;
import com.projet.fiche.Tuteur.Tuteur;
import com.projet.fiche.Tuteur.TuteurDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//La classe DAO est un service qui implémente l'interface DAO, et qui définit les méthodes CRUD, cela assure une sécurité en plus entre le serveur
//et la base de donnée (éviter les injections de donnée dans la BD par exemple)
@Service
public class FicheRenseignementDAO {
    
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;

    //@Autowired permet au Framework Spring de résoudre et injecter le service qui gère les méthodes CRUD de l'objet étudiant
    @Autowired
    private EtudiantDAO etudiantService;

    //@Autowired permet au Framework Spring de résoudre et injecter le service qui gère les méthodes CRUD de l'objet établissement
    @Autowired
    private EtablissementDAO etablissementService;

    //@Autowired permet au Framework Spring de résoudre et injecter le service qui gère les méthodes CRUD de l'objet service de gestion
    @Autowired
    private ServiceGestionDAO gestionService;

    //@Autowired permet au Framework Spring de résoudre et injecter le service qui gère les méthodes CRUD de l'objet tuteur
    @Autowired
    private TuteurDAO tuteurService;

    //@Autowired permet au Framework Spring de résoudre et injecter le service qui gère les méthodes CRUD de l'objet informations du stage
    @Autowired
    private InfosStageDAO infosService;

    //Méthode CRUD findAll() pour récupèrer tous les tuples de la table fiches de renseignement dans la BD
    public ArrayList<FicheRenseignement> findAll() throws RuntimeException {
        //Récupèrer la connexion grâce au service dataSource
        try(Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            //On créé une requête et on sélectionne tous les tuples
            ResultSet results = statement.executeQuery("SELECT * FROM ficheRenseignement");
            //Déclaration d'une liste pour stocker tous les objets étudiants
            ArrayList<FicheRenseignement> fiches = new ArrayList<FicheRenseignement>();

            //Tant qu'on a encore des résultats de la BD
            while(results.next()){
                //On déclare un objet de type fiche de renseignement, peuplement de tous les attributs de cet objet, et ajout dans la liste d'objets fiches
                FicheRenseignement fiche = new FicheRenseignement();
                fiche.setIdFiche(results.getInt("id"));
                fiche.setIdEtudiant(results.getInt("idEtudiant"));
                fiche.setIdEtablissement(results.getInt("idEtablissement"));
                fiche.setIdServiceGestion(results.getInt("idServiceGestion"));
                fiche.setIdTuteur(results.getInt("idTuteur"));
                fiche.setIdInfosStage(results.getInt("idInfosStage"));
                fiche.setDateDeCreation(results.getDate("dateDeCreation"));
                fiche.setFicheValidee(results.getInt("ficheValidee"));

                //Avec l'id etudiant récupéré de l'objet fiche, on va récupèrer l'objet étudiant de la BD
                Etudiant etudiantTrouver = new Etudiant();
                etudiantTrouver = etudiantService.find(fiche.getIdEtudiant());
                fiche.setEtudiant(etudiantTrouver);

                //Avec l'id établissement récupèré de l'objet fiche, on va récupèrer l'objet établissement de la BD
                Etablissement etablissementTrouver = new Etablissement();
                etablissementTrouver = etablissementService.find(fiche.getIdEtablissement());
                fiche.setEtablissement(etablissementTrouver);

                //Avec l'id service de gestion récupéré de l'objet fiche, on va récupèrer l'objet service de gestion de la BD
                ServiceGestion serviceTrouver = new ServiceGestion();
                serviceTrouver = gestionService.find(fiche.getIdServiceGestion());
                fiche.setServiceGestion(serviceTrouver);

                //Avec l'id tuteur récupéré de l'objet fiche, on va récupèrer l'objet tuteur de la BD
                Tuteur tuteurTrouver = new Tuteur();
                tuteurTrouver = tuteurService.find(fiche.getIdTuteur());
                fiche.setTuteur(tuteurTrouver);

                //Avec l'id d'informations d stage récupéré de l'objet fiche, on va récupèrer l'objet informations du stage de la BD
                InfosStage infosTrouver = new InfosStage();
                infosTrouver = infosService.find(fiche.getIdInfosStage());
                fiche.setInfosStage(infosTrouver);

                fiches.add(fiche);
            }
            results.close();
            statement.close();
            //Renvoie la liste des fiches
            return fiches;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //Méthode CRUD find() pour récupèrer une fiche par le nom et prénom de l'étudiant de la BD
    public FicheRenseignement find(String nom, String prenom) throws RuntimeException{
        try(Connection connection = dataSource.getConnection()){
            //Tout d'abord, on va chercher dans la table Etudiants si l'etudiant (nom et prenom) existe dans la BD
            PreparedStatement etudiantStatement = connection.prepareStatement("SELECT * FROM Etudiants where nom = ? AND prenom = ?");
            etudiantStatement.setString(1, nom.toUpperCase());
            etudiantStatement.setString(2, prenom.toUpperCase());
            ResultSet resultatEtudiant = etudiantStatement.executeQuery();
            Etudiant etudiant = new Etudiant();
            while(resultatEtudiant.next()){
                etudiant.setId(resultatEtudiant.getInt("id"));
            }
            //Après avoir cherché l'étudiant, on récupère l'id de l'étudiant, et on cherchera dans la table ficheRenseignement si cet id est présent
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ficheRenseignement where idEtudiant = ?");
            statement.setInt(1, etudiant.getId());
            ResultSet results = statement.executeQuery();
            
            FicheRenseignement fiche = new FicheRenseignement();

            //On récupère la première ligne du résultat retourné, results.next() vaudra false ensuite. Un objet de type fiche de renseignement a été déclaré, peuplé
            //avec le résultat de la sélection SQL, ensuite retourné
            while(results.next()){
                fiche.setIdFiche(results.getInt("id"));
                fiche.setIdEtudiant(results.getInt("idEtudiant"));
                fiche.setIdEtablissement(results.getInt("idEtablissement"));
                fiche.setIdServiceGestion(results.getInt("idServiceGestion"));
                fiche.setIdTuteur(results.getInt("idTuteur"));
                fiche.setIdInfosStage(results.getInt("idInfosStage"));
                fiche.setDateDeCreation(results.getDate("dateDeCreation"));
                fiche.setFicheValidee(results.getInt("ficheValidee"));

                //Avec l'id etudiant récupéré de l'objet fiche, on va récupèrer l'objet étudiant de la BD
                Etudiant etudiantTrouver = new Etudiant();
                etudiantTrouver = etudiantService.find(fiche.getIdEtudiant());
                fiche.setEtudiant(etudiantTrouver);

                //Avec l'id établissement récupèré de l'objet fiche, on va récupèrer l'objet établissement de la BD
                Etablissement etablissementTrouver = new Etablissement();
                etablissementTrouver = etablissementService.find(fiche.getIdEtablissement());
                fiche.setEtablissement(etablissementTrouver);

                //Avec l'id service de gestion récupéré de l'objet fiche, on va récupèrer l'objet service de gestion de la BD
                ServiceGestion serviceTrouver = new ServiceGestion();
                serviceTrouver = gestionService.find(fiche.getIdServiceGestion());
                fiche.setServiceGestion(serviceTrouver);

                //Avec l'id tuteur récupéré de l'objet fiche, on va récupèrer l'objet tuteur de la BD
                Tuteur tuteurTrouver = new Tuteur();
                tuteurTrouver = tuteurService.find(fiche.getIdTuteur());
                fiche.setTuteur(tuteurTrouver);

                //Avec l'id d'informations d stage récupéré de l'objet fiche, on va récupèrer l'objet informations du stage de la BD
                InfosStage infosTrouver = new InfosStage();
                infosTrouver = infosService.find(fiche.getIdInfosStage());
                fiche.setInfosStage(infosTrouver);
            }    
            etudiantStatement.close();
            results.close();
            statement.close();
            return fiche;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    //Méthode CRUD update() pour modifier une fiche par le nom et prénom de l'étudiant de la BD
    public FicheRenseignement update(FicheRenseignement fiche) throws RuntimeException{
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE ficheRenseignement SET ficheValidee = ? where id = ?");
            updateStatement.setInt(1, fiche.getFicheValidee());
            updateStatement.setInt(2, fiche.getIdFiche());
            updateStatement.executeUpdate();
            updateStatement.close();

            FicheRenseignement ficheModifiee = new FicheRenseignement();
            ficheModifiee = this.find(fiche.getEtudiant().getNom(), fiche.getEtudiant().getPrenom());
            return ficheModifiee;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    //Méthode CRUD delete() pour supprimer une fiche par le nom et prénom de l'étudiant de la BD
    public void delete(String nom, String prenom) throws RuntimeException{
        try(Connection connection = dataSource.getConnection()){

            //Tout d'abord, on va chercher dans la table Etudiants si l'etudiant (nom et prenom) existe dans la BD
            PreparedStatement etudiantStatement = connection.prepareStatement("SELECT * FROM Etudiants where nom = ? AND prenom = ?");
            etudiantStatement.setString(1, nom.toUpperCase());
            etudiantStatement.setString(2, prenom.toUpperCase());
            ResultSet resultatEtudiant = etudiantStatement.executeQuery();
            Etudiant etudiant = new Etudiant();
            while(resultatEtudiant.next()){
                etudiant.setId(resultatEtudiant.getInt("id"));
            }

            //Après avoir cherché l'étudiant, on récupère l'id de l'étudiant, et on cherchera dans la table ficheRenseignement si cet id est présent
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ficheRenseignement where idEtudiant = ?");
            statement.setInt(1, etudiant.getId());
            ResultSet results = statement.executeQuery();
            
            FicheRenseignement fiche = new FicheRenseignement();
            while(results.next()){
                fiche.setIdFiche(results.getInt("id"));
                fiche.setIdEtudiant(results.getInt("idEtudiant"));
                fiche.setIdEtablissement(results.getInt("idEtablissement"));
                fiche.setIdServiceGestion(results.getInt("idServiceGestion"));
                fiche.setIdTuteur(results.getInt("idTuteur"));
                fiche.setIdInfosStage(results.getInt("idInfosStage"));
                fiche.setDateDeCreation(results.getDate("dateDeCreation"));

                Statement suppression = connection.createStatement();
                suppression.executeUpdate("DELETE FROM ficheRenseignement WHERE id = " + fiche.getIdFiche());
            
                //Avec l'id etudiant récupéré de l'objet fiche, on va supprimer l'objet étudiant de la BD
                etudiantService.delete(fiche.getIdEtudiant());

                //Avec l'id établissement récupèré de l'objet fiche, on va supprimer l'objet établissement de la BD
                etablissementService.delete(fiche.getIdEtablissement());

                //Avec l'id service de gestion récupéré de l'objet fiche, on va supprimer l'objet service de gestion de la BD
                gestionService.delete(fiche.getIdServiceGestion());

                //Avec l'id tuteur récupéré de l'objet fiche, on va supprimer l'objet tuteur de la BD
                tuteurService.delete(fiche.getIdTuteur());

                //Avec l'id d'informations d stage récupéré de l'objet fiche, on va supprimer l'objet informations du stage de la BD
                infosService.delete(fiche.getIdInfosStage());
            }    
            Statement suppression = connection.createStatement();
            suppression.executeUpdate("DELETE FROM ficheRenseignement WHERE id = " + fiche.getIdFiche());

            suppression.close();
            etudiantStatement.close();
            results.close();
            statement.close();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}