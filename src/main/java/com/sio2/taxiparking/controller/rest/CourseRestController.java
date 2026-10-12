package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class CourseRestController {

	private final CourseService s;

	public CourseRestController(CourseService s) {
		this.s = s;
	}

	@PostMapping
	public ResponseEntity<CourseResponse> add(@Valid @RequestBody CourseRequest r) {
		return ResponseEntity.status(201).body(s.ajouter(r));
	}

	@GetMapping
	public Page<CourseResponse> all(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {
		return s.trouverTous(page, size, "dateHeure", search);
	}

	@PostMapping("/{id}/assign")
	public CourseResponse assign(@PathVariable Long id) {
		return s.attribuer(id);
	}

	@PostMapping("/{id}/cancel")
	public CourseResponse cancel(@PathVariable Long id) {
		return s.annuler(id);
	}
}
