package com.sio2.taxiparking.mapper;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.entity.Vehicule;
public final class VehiculeMapper {
    private VehiculeMapper() {}
    public static VehiculeResponse toResponse(Vehicule v) {
        return new VehiculeResponse(v.getId(), v.getImmatriculation(), v.getMarque(), v.getModele(),
            v.getAnnee(), v.getCapacite(), v.getKilometrage(), v.getStatut());
    }
}
