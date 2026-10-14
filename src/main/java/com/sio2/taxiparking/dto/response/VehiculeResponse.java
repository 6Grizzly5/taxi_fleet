package com.sio2.taxiparking.dto.response;

public record VehiculeResponse(
	Long id,
	String immatriculation,
	String marque,
	String modele,
	Integer annee,
	Integer capacite,
	Double kilometrage,
	com.sio2.taxiparking.enumeration.StatutVehicule statut) {
}
