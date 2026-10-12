package com.sio2.taxiparking.mapper;

import com.sio2.taxiparking.dto.response.ReservationResponse;
import com.sio2.taxiparking.entity.Reservation;

public final class ReservationMapper {

	private ReservationMapper() {
	}

	public static ReservationResponse toResponse(Reservation r) {
		return new ReservationResponse(
				r.getId(),
				r.getUtilisateur().getId(),
				r.getUtilisateur().getNom(),
				r.getVehicule().getId(),
				r.getVehicule().getImmatriculation(),
				r.getPlace().getId(),
				r.getPlace().getNumero(),
				r.getArriveePrevue(),
				r.getDepartPrevu(),
				r.getTarif(),
				r.getStatut());
	}
}
