package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.service.VehiculeService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehiculeRestController {

    private final VehiculeService service;

    public VehiculeRestController(
            VehiculeService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VehiculeResponse> ajouter(
            @Valid @RequestBody VehiculeRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.ajouter(request));
    }

    @GetMapping
    public Page<VehiculeResponse> liste(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "immatriculation") String sort,
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
    public VehiculeResponse detail(
            @PathVariable Long id
    ) {

        return service.trouverParId(id);
    }

    @PutMapping("/{id}")
    public VehiculeResponse modifier(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeRequest request
    ) {

        return service.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id
    ) {

        service.supprimer(id);

        return ResponseEntity.noContent().build();
    }
}