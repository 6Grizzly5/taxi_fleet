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
    private final CourseService service;
    public CourseRestController(CourseService service) { this.service = service; }
    @PostMapping public ResponseEntity<CourseResponse> ajouter(@Valid @RequestBody CourseRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouter(r));
    }
    @GetMapping public Page<CourseResponse> liste(@RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="dateHeure") String sort) {
        return service.trouverTous(page,size,sort);
    }
    @GetMapping("/{id}") public CourseResponse detail(@PathVariable Long id) { return service.trouverParId(id); }
    @PostMapping("/{id}/assign") public CourseResponse assign(@PathVariable Long id) { return service.attribuer(id); }
    @PostMapping("/{id}/start") public CourseResponse start(@PathVariable Long id) { return service.demarrer(id); }
    @PostMapping("/{id}/complete") public CourseResponse complete(@PathVariable Long id) { return service.terminer(id); }
}
