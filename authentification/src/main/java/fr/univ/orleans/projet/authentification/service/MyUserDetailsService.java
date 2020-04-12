package fr.univ.orleans.projet.authentification.service;

import fr.univ.orleans.projet.authentification.exception.UtilisateurDejàExistantException;
import fr.univ.orleans.projet.authentification.exception.UtilisateurIntrouvableException;
import fr.univ.orleans.projet.authentification.modele.Role;
import fr.univ.orleans.projet.authentification.modele.RoleName;
import fr.univ.orleans.projet.authentification.modele.User;
import fr.univ.orleans.projet.authentification.repository.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    private static final String[] ROLES_ADMIN = {"USER","ADMIN"};
    private static final String[] ROLES_USER = {"USER"};

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * identifié l'utilisateur dans la bdd
     * @param login
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("L'utilisateur : ** " + login + "** est introuvable !!")
                );

        return MyUserDetails.build(user);
    }

    /**
     *
     * @return la liste de tous les utilisateurs
     */
    public Collection<User> getAllUsers() {
        return IteratorUtils.toList(this.userRepository.findAll().iterator());
    }

    /**
     * Rechercher un utilisateur par son login
     * @param login
     * @return
     */
    public Optional<User> rechercherUser(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Supprimer un Utilisateur à partir de son ID
     * @param idUser
     * @throws UtilisateurIntrouvableException
     */
    public void supprimerUser(int idUser)  {
        userRepository.deleteById(idUser);
    }





    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}