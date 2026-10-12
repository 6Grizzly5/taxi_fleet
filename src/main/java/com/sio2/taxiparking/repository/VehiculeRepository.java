package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.enumeration.StatutVehicule;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

	Optional<Vehicule> findByImmatriculation(String i);

	boolean existsByImmatriculation(String i);

	boolean existsByImmatriculationAndIdNot(String i, Long id);

	Page<Vehicule> findByImmatriculationContainingIgnoreCase(String i, Pageable p);

	long countByStatut(StatutVehicule s);
}
