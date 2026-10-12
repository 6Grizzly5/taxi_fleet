package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Place;
import com.sio2.taxiparking.enumeration.StatutPlace;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PlaceRepository extends JpaRepository<Place, Long> {

	boolean existsByNumero(String n);

	Optional<Place> findByNumero(String n);

	List<Place> findByStatut(StatutPlace s);

	long countByStatut(StatutPlace s);

	Page<Place> findByNumeroContainingIgnoreCaseOrZoneContainingIgnoreCase(String a, String b, Pageable p);
}
