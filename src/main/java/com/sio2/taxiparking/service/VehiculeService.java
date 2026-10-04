package com.sio2.taxiparking.service;
import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import org.springframework.data.domain.Page;
public interface VehiculeService {
    VehiculeResponse ajouter(VehiculeRequest request);
    Page<VehiculeResponse> trouverTous(int page, int size, String sort, String search);
    VehiculeResponse trouverParId(Long id);
}
