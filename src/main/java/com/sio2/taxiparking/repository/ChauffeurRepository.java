package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Chauffeur;
import com.sio2.taxiparking.enumeration.StatutChauffeur;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {

	Optional<Chauffeur> findByUtilisateurId(Long id);

	Page<Chauffeur> findByNomContainingIgnoreCase(String n, Pageable p);

	List<Chauffeur> findByStatut(StatutChauffeur s);

	boolean existsByNumeroPermis(String n);

	boolean existsByNumeroPermisAndIdNot(String n, Long id);

	long countByStatut(StatutChauffeur s);
}
