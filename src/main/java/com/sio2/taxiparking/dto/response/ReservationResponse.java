package com.sio2.taxiparking.dto.response;

public record ReservationResponse(
	Long id,
	Long utilisateurId,
	String utilisateurNom,
	Long vehiculeId,
	String immatriculation,
	Long placeId,
	String numeroPlace,
	java.time.LocalDateTime arriveePrevue,
	java.time.LocalDateTime departPrevu,
	Double tarif,
	com.sio2.taxiparking.enumeration.StatutReservation statut) {
}
