package com.sio2.taxiparking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record StatisticsResponse(
        LocalDate du, LocalDate au,
        long nombreCourses, long coursesTerminees, long coursesAnnulees,
        BigDecimal chiffreAffaires, BigDecimal commissionEntreprise, BigDecimal revenuChauffeurs,
        double distanceParcourueKm, double tauxAnnulationPourcent, BigDecimal revenuMoyenParCourse,
        String chauffeurLePlusActif, String vehiculeLePlusUtilise,
        Map<String, BigDecimal> chiffreAffairesParChauffeur,
        Map<String, BigDecimal> chiffreAffairesParVehicule,
        Map<String, BigDecimal> chiffreAffairesParJour,
        Map<String, BigDecimal> chiffreAffairesParMois,
        Map<String, Long> activiteParChauffeur,
        Map<String, Long> utilisationParVehicule) {}
