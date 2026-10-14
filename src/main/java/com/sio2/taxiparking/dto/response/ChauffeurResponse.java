package com.sio2.taxiparking.dto.response;

public record ChauffeurResponse(
	Long id,
	String nom,
	String telephone,
	String numeroPermis,
	java.time.LocalDate expirationPermis,
	com.sio2.taxiparking.enumeration.StatutChauffeur statut,
	Long vehiculeId,
	String immatriculation) {
}
