package com.sio2.taxiparking.controller.rest;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.repository.ChauffeurRepository;
import com.sio2.taxiparking.repository.ClientRepository;
import com.sio2.taxiparking.repository.UtilisateurRepository;
import com.sio2.taxiparking.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class CourseRestController {
    private final CourseService service;
    private final UtilisateurRepository users;
    private final ChauffeurRepository drivers;
    private final ClientRepository clients;

    public CourseRestController(CourseService service, UtilisateurRepository users, ChauffeurRepository drivers, ClientRepository clients) {
        this.service = service; this.users = users; this.drivers = drivers; this.clients = clients;
    }

    @PostMapping public ResponseEntity<CourseResponse> add(@Valid @RequestBody CourseRequest request, Authentication authentication) {
        if (authentication == null) throw new BusinessException("Authentification requise");
        boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (admin) return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouter(request));
        boolean clientRole = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"));
        if (!clientRole) throw new BusinessException("Seuls un client ou un administrateur peuvent créer une course");
        Utilisateur user = users.findByEmail(authentication.getName()).orElseThrow(() -> new BusinessException("Utilisateur introuvable"));
        Long clientId = clients.findByUtilisateurId(user.getId()).orElseThrow(() -> new BusinessException("Compte client non associé à un profil client")).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouterPourClient(clientId, request));
    }

    @GetMapping public Page<CourseResponse> all(@RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="dateHeure") String sort,
            @RequestParam(required=false) String search) {
        return service.trouverTous(page, size, sort, search);
    }

    @PostMapping("/{id}/assign") public CourseResponse assign(@PathVariable Long id) { return service.attribuer(id); }
    @PostMapping("/{id}/cancel") public CourseResponse cancel(@PathVariable Long id) { return service.annuler(id); }

    @PostMapping("/{id}/start") public CourseResponse start(@PathVariable Long id, Authentication authentication) {
        return service.demarrer(id, chauffeurConnecte(authentication));
    }

    @PostMapping("/{id}/complete") public CourseResponse complete(@PathVariable Long id, Authentication authentication) {
        return service.terminer(id, chauffeurConnecte(authentication));
    }

    private Long chauffeurConnecte(Authentication authentication) {
        if (authentication == null) throw new BusinessException("Authentification requise");
        Utilisateur user = users.findByEmail(authentication.getName()).orElseThrow(() -> new BusinessException("Utilisateur introuvable"));
        return drivers.findByUtilisateurId(user.getId()).orElseThrow(() -> new BusinessException("Le compte connecté n'est pas associé à un chauffeur")).getId();
    }
}
