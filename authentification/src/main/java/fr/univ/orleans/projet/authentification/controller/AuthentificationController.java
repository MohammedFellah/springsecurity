package fr.univ.orleans.projet.authentification.controller;

import fr.univ.orleans.projet.authentification.modele.Users;
import fr.univ.orleans.projet.authentification.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AuthentificationController {

    @Autowired
    private UsersRepository usersRepository;

    public AuthentificationController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/accueil")
    public String helloEveryone() { return "index"; }

    @GetMapping("/connexion")
    public String connexion(){ return "connexion"; }

    @GetMapping("/deconnexion")
    public String deconnexion(){ return "deconnexion"; }

    @GetMapping("/admin")
    public String helloAdmin() {
        return "admin/index";
    }

    @GetMapping("/profil")
    public String voirProfil() {
        return "profil/index";
    }

    @GetMapping("/billeterie")
    public String accessBilleterie() {
        return "billeterie/index";
    }

    @GetMapping("/admin/users")
    public List<Users> allUsers(){ return this.usersRepository.findAll(); }

}
