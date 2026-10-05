package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Course;
import com.sio2.taxiparking.enumeration.StatutCourse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    Page<Course> findByStatut(
            StatutCourse statut,
            Pageable pageable
    );

    Page<Course> findByClientNomContainingIgnoreCase(
            String nom,
            Pageable pageable
    );

    Page<Course> findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(
            String depart,
            String destination,
            Pageable pageable
    );

    long countByStatut(
            StatutCourse statut
    );

    long countByChauffeurId(
            Long chauffeurId
    );

    long countByVehiculeId(
            Long vehiculeId
    );

    long countByStatutAndDateHeureBetween(
            StatutCourse statut,
            LocalDateTime debut,
            LocalDateTime fin
    );
}