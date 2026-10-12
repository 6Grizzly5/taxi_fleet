package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

	private final ClientService s;

	public ClientRestController(ClientService s) {
		this.s = s;
	}

	@PostMapping
	public ResponseEntity<ClientResponse> add(@Valid @RequestBody ClientRequest r) {
		return ResponseEntity.status(201).body(s.ajouter(r));
	}

	@GetMapping
	public Page<ClientResponse> all(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nom") String sort,
			@RequestParam(required = false) String search) {
		return s.trouverTous(page, size, sort, search);
	}

	@GetMapping("/{id}")
	public ClientResponse one(@PathVariable Long id) {
		return s.trouverParId(id);
	}
}
