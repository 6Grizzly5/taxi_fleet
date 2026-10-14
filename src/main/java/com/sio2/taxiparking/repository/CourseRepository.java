package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Course;
import com.sio2.taxiparking.enumeration.StatutCourse;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(String a, String b, Pageable p);
    Page<Course> findByClientId(Long id, Pageable p);
    Page<Course> findByChauffeurId(Long id, Pageable p);
    Page<Course> findByStatut(StatutCourse s, Pageable p);
    long countByStatut(StatutCourse s);
    long countByClientId(Long id);
    long countByChauffeurId(Long id);
    long countByChauffeurIdAndStatut(Long id, StatutCourse statut);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Course c where c.id = :id")
    Optional<Course> findByIdForUpdate(@Param("id") Long id);
}
