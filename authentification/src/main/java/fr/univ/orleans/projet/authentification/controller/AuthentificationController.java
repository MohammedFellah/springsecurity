package fr.univ.orleans.projet.authentification.controller;

import fr.univ.orleans.projet.authentification.config.JwtProvider;
import fr.univ.orleans.projet.authentification.config.JwtResponse;
import fr.univ.orleans.projet.authentification.modele.Role;
import fr.univ.orleans.projet.authentification.modele.RoleName;
import fr.univ.orleans.projet.authentification.modele.User;
import fr.univ.orleans.projet.authentification.repository.RoleRepository;
import fr.univ.orleans.projet.authentification.repository.UserRepository;
import fr.univ.orleans.projet.authentification.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.NoSuchElementException;
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
    @GetMapping("/home")
    public ResponseEntity<String> accueil() {
        return ResponseEntity.ok().body("*** Bienvenue au MOBE du Groupe 12 ***");
    }

    /**
     * Connexion d'un utilisateur
     *
     * @param user
     * @return TOKEN de connexion
     */
    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@Valid @RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }


    @PostMapping("/inscription")
    public ResponseEntity<String> inscription(@Valid @RequestBody User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            return new ResponseEntity<String>("Erreur -> le nom d'utilisateur est déjà pris!",
                    HttpStatus.BAD_REQUEST);
        }
        // Création du nouvel utilisateur
        User momo = new User(user.getLogin(), encoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();

        //cas 1 -> si le login est admin on attribue le role admin automatiquement
        if (momo.getLogin().equals("admin")){
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);
            roles.add(adminRole);
        }
        //cas 2 -> pour tout autre login on attribue le role user
        else {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
            roles.add(userRole);
        }
        momo.setRoles(roles);
        //sauvgarder l'inscription dans la bdd
        userRepository.save(momo);

        return ResponseEntity.ok().body("** L'utilisater " + momo.getLogin() + " a été enregistré **");
    }

    /**
     * Récupérer la liste de tous les utilisateurs
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getUsers() {
        Iterable<User> users = myUserDetailsService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }


    /**
     * Afficher les informations de l'utilisateur uniquement pour un admin ou pour lui meme.
     * @param login
     * @return
     */
    @GetMapping("/users/{login}")
    public ResponseEntity<Optional<User>> getUtilisateurByLogin(@PathVariable("login") String login) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = myUserDetailsService.rechercherUser(login);
        if(!user.get().getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN))){
            if (!user.get().getLogin().equals(login)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/users/{idUser}")
    public ResponseEntity<String> supprimerUserById(@PathVariable("idUser") int idUser) {
        try {
            myUserDetailsService.supprimerUser(idUser);
        } catch (NoSuchElementException | IllegalArgumentException | EmptyResultDataAccessException e) {
        }
        return ResponseEntity.ok().body("L'utilisateur avec ID = " + idUser + " a été supprimé");
    }
}

