package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.ReservationRequest;
import com.sio2.taxiparking.dto.response.ReservationResponse;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.ReservationMapper;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.ParkingService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.*;

@Service
public class ParkingServiceImpl implements ParkingService {

	private final ReservationRepository rr;
	private final UtilisateurRepository ur;
	private final VehiculeRepository vr;
	private final PlaceRepository pr;

	public ParkingServiceImpl(ReservationRepository rr, UtilisateurRepository ur, VehiculeRepository vr, PlaceRepository pr) {
		this.rr = rr;
		this.ur = ur;
		this.vr = vr;
		this.pr = pr;
	}

	@Transactional
	public ReservationResponse reserver(Long uid, ReservationRequest q) {
		if (q.arriveePrevue().isAfter(q.departPrevu())) {
			throw new BusinessException("Les dates de réservation sont incohérentes");
		}
		Utilisateur u = ur.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
		Vehicule v = vr.findById(q.vehiculeId()).orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
		Place p = q.placeId() == null
				? pr.findByStatut(StatutPlace.LIBRE).stream().findFirst()
						.orElseThrow(() -> new BusinessException("Aucune place libre"))
				: pr.findById(q.placeId()).orElseThrow(() -> new ResourceNotFoundException("Place introuvable"));
		if (p.getStatut() != StatutPlace.LIBRE) throw new BusinessException("Cette place n'est pas libre");
		Reservation r = new Reservation();
		r.setUtilisateur(u);
		r.setVehicule(v);
		r.setPlace(p);
		r.setArriveePrevue(q.arriveePrevue());
		r.setDepartPrevu(q.departPrevu());
		long h = Math.max(1, Duration.between(q.arriveePrevue(), q.departPrevu()).toHours());
		r.setTarif(h * 2000.0);
		r.setStatut(StatutReservation.CONFIRMEE);
		p.setStatut(StatutPlace.RESERVEE);
		return ReservationMapper.toResponse(rr.save(r));
	}

	public Page<ReservationResponse> reservations(int p, int s) {
		return rr.findAll(PageRequest.of(p, s, Sort.by("arriveePrevue").descending()))
				.map(ReservationMapper::toResponse);
	}

	public List<ReservationResponse> reservationsUtilisateur(Long id) {
		return rr.findByUtilisateurIdOrderByArriveePrevueDesc(id).stream()
				.map(ReservationMapper::toResponse).toList();
	}

	@Transactional
	public void annuler(Long id, Long uid) {
		Reservation r = rr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));
		if (!r.getUtilisateur().getId().equals(uid)) throw new BusinessException("Réservation non autorisée");
		r.setStatut(StatutReservation.ANNULEE);
		r.getPlace().setStatut(StatutPlace.LIBRE);
		rr.save(r);
	}
}
