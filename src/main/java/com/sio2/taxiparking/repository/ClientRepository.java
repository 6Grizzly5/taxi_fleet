package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Client;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByUtilisateurId(Long id);

	boolean existsByEmail(String e);

	boolean existsByEmailAndIdNot(String e, Long id);

	Page<Client> findByNomContainingIgnoreCase(String n, Pageable p);
}
