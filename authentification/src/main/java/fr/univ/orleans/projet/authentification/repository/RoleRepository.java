package fr.univ.orleans.projet.authentification.repository;

import fr.univ.orleans.projet.authentification.modele.Role;
import fr.univ.orleans.projet.authentification.modele.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(RoleName  name);
}
