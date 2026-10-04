package com.sio2.taxiparking.service.impl;
import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.entity.Client;
import com.sio2.taxiparking.mapper.ClientMapper;
import com.sio2.taxiparking.repository.ClientRepository;
import com.sio2.taxiparking.service.ClientService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    public ClientServiceImpl(ClientRepository repository) { this.repository = repository; }
    public ClientResponse ajouter(ClientRequest r) {
        Client c = new Client();
        c.setNom(r.nom()); c.setTelephone(r.telephone()); c.setEmail(r.email());
        return ClientMapper.toResponse(repository.save(c));
    }
    public Page<ClientResponse> trouverTous(int page, int size, String sort, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNomContainingIgnoreCase(search == null ? "" : search, pageable).map(ClientMapper::toResponse);
    }
    public ClientResponse trouverParId(Long id) {
        return ClientMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new com.sio2.taxiparking.exception.ResourceNotFoundException("Client introuvable")));
    }
}
