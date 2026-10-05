package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.entity.Client;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.exception.ResourceNotFoundException;
import com.sio2.taxiparking.mapper.ClientMapper;
import com.sio2.taxiparking.repository.ClientRepository;
import com.sio2.taxiparking.service.ClientService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientResponse ajouter(ClientRequest request) {

        String email = request.email().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new BusinessException(
                    "Un client utilise déjà cette adresse email"
            );
        }

        Client client = new Client();

        client.setNom(request.nom().trim());
        client.setTelephone(request.telephone().trim());
        client.setEmail(email);

        return ClientMapper.toResponse(
                repository.save(client)
        );
    }

    @Override
    public ClientResponse modifier(
            Long id,
            ClientRequest request
    ) {

        Client client = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client introuvable"
                        )
                );

        String email = request.email().trim().toLowerCase();

        if (repository.existsByEmailAndIdNot(email, id)) {
            throw new BusinessException(
                    "Un autre client utilise déjà cette adresse email"
            );
        }

        client.setNom(request.nom().trim());
        client.setTelephone(request.telephone().trim());
        client.setEmail(email);

        return ClientMapper.toResponse(
                repository.save(client)
        );
    }

    @Override
    public Page<ClientResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort).ascending()
        );

        String recherche =
                search == null
                        ? ""
                        : search.trim();

        return repository
                .findByNomContainingIgnoreCase(
                        recherche,
                        pageable
                )
                .map(ClientMapper::toResponse);
    }

    @Override
    public ClientResponse trouverParId(Long id) {

        return ClientMapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Client introuvable"
                                )
                        )
        );
    }

    @Override
    public void supprimer(Long id) {

        Client client = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client introuvable"
                        )
                );

        repository.delete(client);
    }
}