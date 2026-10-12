package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import com.sio2.taxiparking.service.ChauffeurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class ChauffeurRestController {

	private final ChauffeurService s;

	public ChauffeurRestController(ChauffeurService s) {
		this.s = s;
	}

	@PostMapping
	public ResponseEntity<ChauffeurResponse> add(@Valid @RequestBody ChauffeurRequest r) {
		return ResponseEntity.status(201).body(s.ajouter(r));
	}

	@GetMapping
	public Page<ChauffeurResponse> all(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nom") String sort,
			@RequestParam(required = false) String search) {
		return s.trouverTous(page, size, sort, search);
	}

	@GetMapping("/{id}")
	public ChauffeurResponse one(@PathVariable Long id) {
		return s.trouverParId(id);
	}
}
