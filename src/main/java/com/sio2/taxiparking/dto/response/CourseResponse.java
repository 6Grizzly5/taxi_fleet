package com.sio2.taxiparking.dto.response;

public record CourseResponse(
	Long id,
	Long clientId,
	String clientNom,
	Long chauffeurId,
	String chauffeurNom,
	Long vehiculeId,
	String immatriculation,
	String depart,
	String destination,
	Integer nombrePersonnes,
	Double distance,
	Double tarif,
	java.time.LocalDateTime dateHeure,
	com.sio2.taxiparking.enumeration.StatutCourse statut) {
}
