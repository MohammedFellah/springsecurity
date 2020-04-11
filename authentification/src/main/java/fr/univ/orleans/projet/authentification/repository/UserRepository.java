package fr.univ.orleans.projet.authentification.repository;

import fr.univ.orleans.projet.authentification.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    Boolean existsByLogin(String login);

}
