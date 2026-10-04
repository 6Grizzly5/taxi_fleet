package com.sio2.taxiparking.dto.response;
import com.sio2.taxiparking.enumeration.StatutVehicule;
public record VehiculeResponse(Long id, String immatriculation, String marque, String modele,
    Integer annee, Integer capacite, Double kilometrage, StatutVehicule statut) {}
