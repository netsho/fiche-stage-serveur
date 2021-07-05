package com.projet.fiche.ServiceGestion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/services")
public class ServiceGestionController {
    
    @Autowired
    private ServiceGestionDAO serviceDAO;

    @GetMapping("/")
    public ArrayList<ServiceGestion> findAll(HttpServletResponse response){
        try{
            return serviceDAO.findAll();
        } catch (Exception e){
            response.setStatus(500);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{serviceMail}")
    public ServiceGestion find(@PathVariable(value="serviceMail") String serviceMail, HttpServletResponse response){
        try{
            ServiceGestion serviceObject = new ServiceGestion();
            serviceObject = serviceDAO.find(serviceMail);

            //Erreur 404 si l'adresse n'existe pas dans la BD
            if(serviceObject.getMail() == null){
                System.out.println("L'adresse n'existe pas !");
                response.setStatus(404);
                return null;
            } else {
                return serviceObject;
            }
        } catch(Exception e){
            response.setStatus(500);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @PostMapping("/{serviceMail}")
    public ServiceGestion create(@PathVariable(value="serviceMail") String serviceMail, @RequestBody ServiceGestion serviceObject, HttpServletResponse response){
        try {

            //une erreur 412 si le mail du service de gestion dans l'URL n'est pas le même que celui du service de gestion dans le corp de la requête.
           if(!serviceMail.equals(serviceObject.getMail())){
               System.out.println("Request body not equivalent to variable path : " + serviceMail + " != " + serviceObject.getMail());
               response.setStatus(412);
               return null;
           }

           //Une erreur 403 si le service de gestion existe déjà dans la BD
           else if(find(serviceMail, response) == null){
               ServiceGestion serviceInsere = new ServiceGestion();
               serviceInsere = serviceDAO.create(serviceObject);
               response.setStatus(200);
               return serviceInsere;
           } else {
               System.out.println("Service de gestion already exists !");
               response.setStatus(403);
               return null;
           }

       } catch (Exception e) {
           response.setStatus(500);
           System.err.println(e.getMessage());
           return null;
       }
    }

    @PutMapping("/{serviceMail}")
    public ServiceGestion update(@PathVariable(value="serviceMail") String serviceMail, @RequestBody ServiceGestion serviceObject, HttpServletResponse response){
        try {

            ServiceGestion serviceExiste = new ServiceGestion();
            serviceExiste = serviceDAO.find(serviceMail);

            //Une erreur 403 si le service de gestion n'existe pas dans la BD
            if(serviceExiste.getMail() == null){
                System.out.println("Service de gestion does not exist !");
                response.setStatus(404);
                return null;
            } else {
                ServiceGestion serviceModifie = new ServiceGestion();
                serviceObject.setId(serviceExiste.getId());
                serviceModifie = serviceDAO.update(serviceObject);
                return serviceModifie;
            }

        } catch (Exception e) {
            response.setStatus(500);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{serviceMail}")
    public void delete(@PathVariable(value="serviceMail") String serviceMail, HttpServletResponse response){
        try {
            ServiceGestion serviceObject = new ServiceGestion();
            serviceObject = serviceDAO.find(serviceMail);

            //Erreur 404 si le service de gestion n'existe pas dans la BD
            if(serviceObject.getMail() == null){
                System.out.println("Le service de gestion n'existe pas !");
                response.setStatus(404);
            } else {
                serviceDAO.delete(serviceMail);
            }
            
        } catch (Exception e){
            response.setStatus(500);
            System.err.println(e.getMessage());
        }
    }
}