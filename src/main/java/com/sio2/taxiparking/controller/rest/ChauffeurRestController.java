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
    private final ChauffeurService service;
    public ChauffeurRestController(ChauffeurService service) { this.service = service; }
    @PostMapping public ResponseEntity<ChauffeurResponse> ajouter(@Valid @RequestBody ChauffeurRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouter(r));
    }
    @GetMapping public Page<ChauffeurResponse> liste(@RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="nom") String sort,
        @RequestParam(required=false) String search) {
        return service.trouverTous(page,size,sort,search);
    }
    @GetMapping("/{id}") public ChauffeurResponse detail(@PathVariable Long id) { return service.trouverParId(id); }
}
