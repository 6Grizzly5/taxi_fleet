package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.enumeration.StatutVehicule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository
        extends JpaRepository<Vehicule, Long> {

    Page<Vehicule> findByImmatriculationContainingIgnoreCase(
            String immatriculation,
            Pageable pageable
    );

    boolean existsByImmatriculation(
            String immatriculation
    );

    boolean existsByImmatriculationAndIdNot(
            String immatriculation,
            Long id
    );

    long countByStatut(
            StatutVehicule statut
    );
}