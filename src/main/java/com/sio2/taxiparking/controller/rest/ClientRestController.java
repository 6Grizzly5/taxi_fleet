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
    private final ClientService service;
    public ClientRestController(ClientService service) { this.service = service; }
    @PostMapping public ResponseEntity<ClientResponse> ajouter(@Valid @RequestBody ClientRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouter(r));
    }
    @GetMapping public Page<ClientResponse> liste(@RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="nom") String sort,
        @RequestParam(required=false) String search) { return service.trouverTous(page,size,sort,search); }
    @GetMapping("/{id}") public ClientResponse detail(@PathVariable Long id) { return service.trouverParId(id); }
}
