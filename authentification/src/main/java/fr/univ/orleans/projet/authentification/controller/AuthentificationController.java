package fr.univ.orleans.projet.authentification.controller;

import fr.univ.orleans.projet.authentification.exception.UtilisateurDejàExistantException;
import fr.univ.orleans.projet.authentification.modele.User;
import fr.univ.orleans.projet.authentification.repository.UserRepository;
import fr.univ.orleans.projet.authentification.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public AuthentificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Page d'accueil
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<String> accueil(){
        return ResponseEntity.ok().body("bienvenue au mobe");
    }

    /**
     *  Afficher tous les utilisateurs
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getUtilisateurs(){
        Iterable<User> users = myUserDetailsService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Inscription d'un nouvel utilisateur
     * @param user
     * @return
     * @throws UtilisateurDejàExistantException
     */
    @PostMapping("/inscription")
    public ResponseEntity<User> inscription(@RequestBody User user) throws UtilisateurDejàExistantException {
        return ResponseEntity.ok().body(this.myUserDetailsService.createUser(user));

    }

    /**
     * Rechercher un utilisateur par son login
     * @param login
     * @return
     */
    @GetMapping("/users/{login}")
    public ResponseEntity<Optional<User>> getUtilisateurByLogin(@PathVariable("login") String login){
      Optional<User> user = myUserDetailsService.rechercherUser(login);
       return ResponseEntity.ok().body(user);
    }

    /**
     * Supprimer un utilisateur à partir de son ID
     * @param idUser
     */
    @DeleteMapping("/users/{idUser}")
    public ResponseEntity<String> supprimerUserById(@PathVariable("idUser") int idUser) {
        try {
            myUserDetailsService.supprimerUser(idUser);
        }catch (NoSuchElementException | IllegalArgumentException | EmptyResultDataAccessException e){
        }
        return ResponseEntity.ok().body("L'utilisateur avec ID = "+idUser+" a été supprimé");
    }

    @PostMapping("/connexion")
    public String connexion(@RequestBody String login){
        myUserDetailsService.loadUserByUsername(login);
        return "vous etes bien connecté + "+login;
    }

}
