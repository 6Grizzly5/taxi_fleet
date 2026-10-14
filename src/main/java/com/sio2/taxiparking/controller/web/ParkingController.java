package com.sio2.taxiparking.controller.web;

import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.ParkingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ParkingController {

	private final UtilisateurRepository ur;
	private final VehiculeRepository vr;
	private final PlaceRepository pr;
	private final ParkingService ps;

	public ParkingController(UtilisateurRepository ur, VehiculeRepository vr, PlaceRepository pr, ParkingService ps) {
		this.ur = ur;
		this.vr = vr;
		this.pr = pr;
		this.ps = ps;
	}

	@GetMapping("/client/parking")
	public String client(Model m, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		m.addAttribute("vehicules", vr.findAll());
		m.addAttribute("places", pr.findByStatut(com.sio2.taxiparking.enumeration.StatutPlace.LIBRE));
		m.addAttribute("reservations", ps.reservationsUtilisateur(u.getId()));
		m.addAttribute("request", new com.sio2.taxiparking.dto.request.ReservationRequest(
				null, null, java.time.LocalDateTime.now().plusHours(1), java.time.LocalDateTime.now().plusHours(2)));
		return "parking/client";
	}

	@PostMapping("/client/parking/reserver")
	public String reserve(com.sio2.taxiparking.dto.request.ReservationRequest r, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		ps.reserver(u.getId(), r);
		return "redirect:/client/parking";
	}

	@PostMapping("/client/parking/{id}/annuler")
	public String cancel(@PathVariable Long id, Authentication a) {
		Utilisateur u = ur.findByEmail(a.getName()).orElseThrow();
		ps.annuler(id, u.getId());
		return "redirect:/client/parking";
	}
}
