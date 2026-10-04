package com.sio2.taxiparking.mapper;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.Course;
public final class CourseMapper {
    private CourseMapper() {}
    public static CourseResponse toResponse(Course c) {
        return new CourseResponse(c.getId(),
            c.getClient().getId(), c.getClient().getNom(),
            c.getChauffeur() == null ? null : c.getChauffeur().getId(),
            c.getChauffeur() == null ? null : c.getChauffeur().getNom(),
            c.getVehicule() == null ? null : c.getVehicule().getId(),
            c.getVehicule() == null ? null : c.getVehicule().getImmatriculation(),
            c.getDepart(), c.getDestination(), c.getDistance(), c.getTarif(),
            c.getDateHeure(), c.getStatut());
    }
}
