package fr.univ.orleans.projet.authentification.service;

import fr.univ.orleans.projet.authentification.exception.UtilisateurDejàExistantException;
import fr.univ.orleans.projet.authentification.exception.UtilisateurIntrouvableException;
import fr.univ.orleans.projet.authentification.modele.MyUserDetails;
import fr.univ.orleans.projet.authentification.modele.Role;
import fr.univ.orleans.projet.authentification.modele.User;
import fr.univ.orleans.projet.authentification.repository.RoleRepository;
import fr.univ.orleans.projet.authentification.repository.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    public MyUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * identifié l'utilisateur dans la bdd
     * @param login
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> optionalUsers = userRepository.findByLogin(login);
        optionalUsers
                .orElseThrow(() -> new UsernameNotFoundException(login +"Utilisateur Introuvable!"));
        return optionalUsers
                .map(MyUserDetails::new).get();

    }

    /**
     * Création d'un utilisateur
     * @param user
     * @return
     * @throws UtilisateurDejàExistantException
     */
    public User createUser(User user) throws UtilisateurDejàExistantException {
        if (userRepository.existsById(user.getId())) {
            throw new UtilisateurDejàExistantException();
        }
        return userRepository.save(user);
    }
    /**
     *
     * @return la liste de tous les utilisateurs
     */
    public Collection<User> getAllUsers(){
        return IteratorUtils.toList(this.userRepository.findAll().iterator());
    }

    /**
     * Supprimer un Utilisateur à partir de son ID
     * @param idUser
     * @throws UtilisateurIntrouvableException
     */
    public void supprimerUser(int idUser)  {
        userRepository.deleteById(idUser);
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
     * Afficher tous les roles
     * @return
     */
    public Collection<Role> getAllRoles(){
        return IteratorUtils.toList(this.roleRepository.findAll().iterator());
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}