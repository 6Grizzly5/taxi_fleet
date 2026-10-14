package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.ClientRequest;
import com.sio2.taxiparking.dto.response.ClientResponse;
import com.sio2.taxiparking.entity.Client;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.ClientMapper;
import com.sio2.taxiparking.repository.ClientRepository;
import com.sio2.taxiparking.service.ClientService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository r;

	public ClientServiceImpl(ClientRepository r) {
		this.r = r;
	}

	public ClientResponse ajouter(ClientRequest q) {
		if (r.existsByEmail(q.email().trim().toLowerCase())) throw new BusinessException("Email déjà utilisé");
		Client c = new Client();
		c.setNom(q.nom().trim());
		c.setTelephone(q.telephone().trim());
		c.setEmail(q.email().trim().toLowerCase());
		return ClientMapper.toResponse(r.save(c));
	}

	public Page<ClientResponse> trouverTous(int p, int s, String sort, String search) {
		Pageable x = PageRequest.of(Math.max(0, p), Math.max(1, s), Sort.by(sort == null ? "nom" : sort).ascending());
		return r.findByNomContainingIgnoreCase(search == null ? "" : search.trim(), x)
				.map(ClientMapper::toResponse);
	}

	public ClientResponse trouverParId(Long id) {
		return ClientMapper.toResponse(r.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client introuvable")));
	}
}
