package fr.univ.orleans.projet.authentification.controller;

import fr.univ.orleans.projet.authentification.config.JwtProvider;
import fr.univ.orleans.projet.authentification.config.JwtResponse;
import fr.univ.orleans.projet.authentification.form.RegisterForm;
import fr.univ.orleans.projet.authentification.modele.Role;
import fr.univ.orleans.projet.authentification.modele.RoleName;
import fr.univ.orleans.projet.authentification.modele.User;
import fr.univ.orleans.projet.authentification.repository.RoleRepository;
import fr.univ.orleans.projet.authentification.repository.UserRepository;
import fr.univ.orleans.projet.authentification.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthentificationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public AuthentificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Page d'accueil
     *
     * @return
     */
    @GetMapping("/panel")
    public ResponseEntity<String> accueil() {
        return ResponseEntity.ok().body("bienvenue ADMIN");
    }

    //connexion
    @PostMapping("/connexion")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/inscription")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            return new ResponseEntity<String>("Erreur -> le nom d'utilisateur est déjà pris!",
                    HttpStatus.BAD_REQUEST);
        }
        // Creating user's account
        User momo = new User(user.getLogin(), encoder.encode(user.getPassword()));
        //attribuer le role utilsateur pour chaque nouvelle inscription
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
        roles.add(userRole);

        momo.setRoles(roles);
        //sauvgarder l'inscription dans la bdd
        userRepository.save(momo);

        return ResponseEntity.ok().body("** L'utilisater "+user.getLogin()+" a été enregistré **");
    }
}


   /* *//**
     *  Afficher tous les utilisateurs
     * @return
     *//*
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getUtilisateurs(){
        Iterable<User> users = myUserDetailsService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    *//**
     * Inscription d'un nouvel utilisateur
     * @param user
     * @return
     * @throws UtilisateurDejàExistantException
     *//*
    @PostMapping("/inscription")
    public ResponseEntity<User> inscription(@RequestBody User user) throws UtilisateurDejàExistantException {
        return ResponseEntity.ok().body(this.myUserDetailsService.createUser(user));

    }

    *//**
     * Rechercher un utilisateur par son login
     * @param login
     * @return
     *//*
    @GetMapping("/users/{login}")
    public ResponseEntity<Optional<User>> getUtilisateurByLogin(@PathVariable("login") String login){
      Optional<User> user = myUserDetailsService.rechercherUser(login);
       return ResponseEntity.ok().body(user);
    }

    *//**
     * Supprimer un utilisateur à partir de son ID
     * @param idUser
     *//*
    @DeleteMapping("/users/{idUser}")
    public ResponseEntity<String> supprimerUserById(@PathVariable("idUser") int idUser) {
        try {
            myUserDetailsService.supprimerUser(idUser);
        }catch (NoSuchElementException | IllegalArgumentException | EmptyResultDataAccessException e){
        }
        return ResponseEntity.ok().body("L'utilisateur avec ID = "+idUser+" a été supprimé");
    }*/

