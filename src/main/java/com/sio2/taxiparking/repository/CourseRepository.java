package com.sio2.taxiparking.repository;

import com.sio2.taxiparking.entity.Course;
import com.sio2.taxiparking.enumeration.StatutCourse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Page<Course> findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(String a, String b, Pageable p);

	Page<Course> findByClientId(Long id, Pageable p);

	Page<Course> findByChauffeurId(Long id, Pageable p);

	Page<Course> findByStatut(StatutCourse s, Pageable p);

	long countByStatut(StatutCourse s);

	long countByClientId(Long id);

	long countByChauffeurId(Long id);
}
