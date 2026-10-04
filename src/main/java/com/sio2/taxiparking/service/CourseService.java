package com.sio2.taxiparking.service;
import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
public interface CourseService {
    CourseResponse ajouter(CourseRequest request);
    Page<CourseResponse> trouverTous(int page, int size, String sort);
    CourseResponse trouverParId(Long id);
    CourseResponse attribuer(Long id);
    CourseResponse demarrer(Long id);
    CourseResponse terminer(Long id);
}
