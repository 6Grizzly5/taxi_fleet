package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.service.CourseService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class CourseRestController {

    private final CourseService service;

    public CourseRestController(
            CourseService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> ajouter(
            @Valid @RequestBody CourseRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.ajouter(request));
    }

    @GetMapping
    public Page<CourseResponse> liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateHeure") String sort,
            @RequestParam(required = false) String search
    ) {

        return service.trouverTous(
                page,
                size,
                sort,
                search
        );
    }

    @GetMapping("/{id}")
    public CourseResponse detail(
            @PathVariable Long id
    ) {

        return service.trouverParId(id);
    }

    @PostMapping("/{id}/assign")
    public CourseResponse assigner(
            @PathVariable Long id
    ) {

        return service.attribuer(id);
    }

    @PostMapping("/{id}/start")
    public CourseResponse demarrer(
            @PathVariable Long id
    ) {

        return service.demarrer(id);
    }

    @PostMapping("/{id}/complete")
    public CourseResponse terminer(
            @PathVariable Long id
    ) {

        return service.terminer(id);
    }

    @PostMapping("/{id}/cancel")
    public CourseResponse annuler(
            @PathVariable Long id
    ) {

        return service.annuler(id);
    }
}