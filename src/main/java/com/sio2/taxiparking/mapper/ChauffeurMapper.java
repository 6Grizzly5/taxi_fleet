package com.sio2.taxiparking.mapper;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import com.sio2.taxiparking.entity.Chauffeur;
public final class ChauffeurMapper {
    private ChauffeurMapper() {}
    public static ChauffeurResponse toResponse(Chauffeur c) {
        return new ChauffeurResponse(c.getId(), c.getNom(), c.getTelephone(), c.getNumeroPermis(),
            c.getExpirationPermis(), c.getStatut(), c.getDateEmbauche(),
            c.getVehicule() == null ? null : c.getVehicule().getId());
    }
}
