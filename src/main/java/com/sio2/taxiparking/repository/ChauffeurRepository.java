package com.sio2.taxiparking.repository;
import com.sio2.taxiparking.entity.Chauffeur;
import com.sio2.taxiparking.enumeration.StatutChauffeur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
    Page<Chauffeur> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    List<Chauffeur> findByStatut(StatutChauffeur statut);
    boolean existsByNumeroPermis(String numeroPermis);
    long countByStatut(StatutChauffeur statut);
    List<Chauffeur> findByStatutAndExpirationPermisAfter(StatutChauffeur statut, LocalDate date);
}
