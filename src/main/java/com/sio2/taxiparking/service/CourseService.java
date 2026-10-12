package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

public interface CourseService {
    CourseResponse ajouter(CourseRequest request);
    CourseResponse ajouterPourClient(Long clientId, CourseRequest request);
    Page<CourseResponse> trouverTous(int page, int size, String sort, String search);
    Page<CourseResponse> pourClient(Long clientId, int page, int size);
    Page<CourseResponse> pourChauffeur(Long chauffeurId, int page, int size);
    CourseResponse attribuer(Long id);
    CourseResponse accepter(Long id, Long chauffeurId);
    CourseResponse refuser(Long id, Long chauffeurId);
    CourseResponse demarrer(Long id, Long chauffeurId);
    CourseResponse terminer(Long id, Long chauffeurId);
    CourseResponse annuler(Long id);
}
