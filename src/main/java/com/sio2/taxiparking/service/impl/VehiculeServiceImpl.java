package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.VehiculeMapper;
import com.sio2.taxiparking.repository.VehiculeRepository;
import com.sio2.taxiparking.service.VehiculeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class VehiculeServiceImpl implements VehiculeService {

	private final VehiculeRepository r;

	public VehiculeServiceImpl(VehiculeRepository r) {
		this.r = r;
	}

	public VehiculeResponse ajouter(VehiculeRequest q) {
		String i = q.immatriculation().trim().toUpperCase();
		if (r.existsByImmatriculation(i)) throw new BusinessException("Immatriculation déjà utilisée");
		Vehicule v = new Vehicule();
		v.setImmatriculation(i);
		v.setMarque(q.marque().trim());
		v.setModele(q.modele().trim());
		v.setAnnee(q.annee());
		v.setCapacite(q.capacite());
		v.setKilometrage(q.kilometrage());
		return VehiculeMapper.toResponse(r.save(v));
	}

	public VehiculeResponse modifier(Long id, VehiculeRequest q) {
		Vehicule v = r.findById(id).orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
		String i = q.immatriculation().trim().toUpperCase();
		if (r.existsByImmatriculationAndIdNot(i, id)) throw new BusinessException("Immatriculation déjà utilisée");
		v.setImmatriculation(i);
		v.setMarque(q.marque().trim());
		v.setModele(q.modele().trim());
		v.setAnnee(q.annee());
		v.setCapacite(q.capacite());
		v.setKilometrage(q.kilometrage());
		return VehiculeMapper.toResponse(r.save(v));
	}

	public Page<VehiculeResponse> trouverTous(int p, int s, String sort, String search) {
		Pageable x = PageRequest.of(
				Math.max(0, p), Math.max(1, s), Sort.by(sort == null ? "immatriculation" : sort).ascending());
		return r.findByImmatriculationContainingIgnoreCase(search == null ? "" : search.trim(), x)
				.map(VehiculeMapper::toResponse);
	}

	public VehiculeResponse trouverParId(Long id) {
		return VehiculeMapper.toResponse(r.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable")));
	}

	public void supprimer(Long id) {
		Vehicule v = r.findById(id).orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
		if (v.getStatut() == com.sio2.taxiparking.enumeration.StatutVehicule.EN_COURSE) {
			throw new BusinessException("Véhicule actuellement en course");
		}
		r.delete(v);
	}
}
