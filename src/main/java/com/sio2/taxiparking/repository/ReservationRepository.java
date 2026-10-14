package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Reservation;
import com.sio2.taxiparking.enumeration.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByUtilisateurIdOrderByArriveePrevueDesc(Long id);

	long countByStatut(StatutReservation s);
}
