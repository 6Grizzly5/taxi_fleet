package com.sio2.taxiparking.dto.response;
import com.sio2.taxiparking.enumeration.StatutCourse;
import java.time.LocalDateTime;
public record CourseResponse(Long id, Long clientId, String clientNom, Long chauffeurId,
    String chauffeurNom, Long vehiculeId, String immatriculation, String depart,
    String destination, Double distance, Double tarif, LocalDateTime dateHeure,
    StatutCourse statut) {}
