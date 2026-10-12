package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.ReservationRequest;
import com.sio2.taxiparking.dto.response.ReservationResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ParkingService {
    ReservationResponse reserver(Long utilisateurId, ReservationRequest request);
    Page<ReservationResponse> reservations(int page, int size);
    List<ReservationResponse> reservationsUtilisateur(Long id);
    void annuler(Long id, Long utilisateurId);
}
