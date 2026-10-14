package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Chauffeur;
import com.sio2.taxiparking.enumeration.StatutChauffeur;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {

	Optional<Chauffeur> findByUtilisateurId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Chauffeur c where c.id = :id")
    Optional<Chauffeur> findByIdForUpdate(@Param("id") Long id);

	Page<Chauffeur> findByNomContainingIgnoreCase(String n, Pageable p);

	List<Chauffeur> findByStatut(StatutChauffeur s);

	boolean existsByNumeroPermis(String n);

	boolean existsByNumeroPermisAndIdNot(String n, Long id);

	long countByStatut(StatutChauffeur s);
}
