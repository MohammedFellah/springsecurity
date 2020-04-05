package fr.univ.orleans.projet.authentification.service;

import fr.univ.orleans.projet.authentification.modele.MyUserDetails;
import fr.univ.orleans.projet.authentification.modele.Users;
import fr.univ.orleans.projet.authentification.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       Optional<Users> optionalUsers = usersRepository.findByUserName(userName);

        optionalUsers
                .orElseThrow(() -> new UsernameNotFoundException(userName+"Utilisateur Introuvable!"));
        return optionalUsers
                .map(MyUserDetails::new).get();

    }
}
