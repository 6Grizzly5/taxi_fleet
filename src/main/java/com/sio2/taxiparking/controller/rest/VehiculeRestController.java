package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.service.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehiculeRestController {

	private final VehiculeService s;

	public VehiculeRestController(VehiculeService s) {
		this.s = s;
	}

	@PostMapping
	public ResponseEntity<VehiculeResponse> add(@Valid @RequestBody VehiculeRequest r) {
		return ResponseEntity.status(201).body(s.ajouter(r));
	}

	@GetMapping
	public Page<VehiculeResponse> all(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "immatriculation") String sort,
			@RequestParam(required = false) String search) {
		return s.trouverTous(page, size, sort, search);
	}

	@GetMapping("/{id}")
	public VehiculeResponse one(@PathVariable Long id) {
		return s.trouverParId(id);
	}
}
