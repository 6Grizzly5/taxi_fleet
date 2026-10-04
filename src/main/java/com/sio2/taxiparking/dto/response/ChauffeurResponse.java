package com.sio2.taxiparking.dto.response;
import com.sio2.taxiparking.enumeration.StatutChauffeur;
import java.time.LocalDate;
public record ChauffeurResponse(Long id, String nom, String telephone, String numeroPermis,
    LocalDate expirationPermis, StatutChauffeur statut, LocalDate dateEmbauche, Long vehiculeId) {}
