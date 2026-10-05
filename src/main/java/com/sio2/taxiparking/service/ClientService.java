package com.sio2.taxiparking.service;

import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import org.springframework.data.domain.Page;

public interface ClientService {

    ClientResponse ajouter(ClientRequest request);

    ClientResponse modifier(Long id, ClientRequest request);

    Page<ClientResponse> trouverTous(
            int page,
            int size,
            String sort,
            String search
    );

    ClientResponse trouverParId(Long id);

    void supprimer(Long id);
}