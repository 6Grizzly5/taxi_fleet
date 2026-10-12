package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.ChauffeurRequest;
import com.sio2.taxiparking.dto.response.ChauffeurResponse;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.ChauffeurMapper;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.ChauffeurService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ChauffeurServiceImpl implements ChauffeurService {

	private final ChauffeurRepository r;
	private final VehiculeRepository vr;

	public ChauffeurServiceImpl(ChauffeurRepository r, VehiculeRepository vr) {
		this.r = r;
		this.vr = vr;
	}

	public ChauffeurResponse ajouter(ChauffeurRequest q) {
		if (r.existsByNumeroPermis(q.numeroPermis().trim())) throw new BusinessException("Permis déjà utilisé");
		Vehicule v = q.vehiculeId() == null
				? null
				: vr.findById(q.vehiculeId()).orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
		Chauffeur c = new Chauffeur();
		c.setNom(q.nom().trim());
		c.setTelephone(q.telephone().trim());
		c.setNumeroPermis(q.numeroPermis().trim());
		c.setExpirationPermis(q.expirationPermis());
		c.setDateEmbauche(q.dateEmbauche());
		c.setStatut(StatutChauffeur.INDISPONIBLE);
		c.setVehicule(v);
		return ChauffeurMapper.toResponse(r.save(c));
	}

	public Page<ChauffeurResponse> trouverTous(int p, int s, String sort, String search) {
		Pageable x = PageRequest.of(Math.max(0, p), Math.max(1, s), Sort.by(sort == null ? "nom" : sort).ascending());
		return r.findByNomContainingIgnoreCase(search == null ? "" : search.trim(), x)
				.map(ChauffeurMapper::toResponse);
	}

	public ChauffeurResponse trouverParId(Long id) {
		return ChauffeurMapper.toResponse(r.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable")));
	}

	public ChauffeurResponse valider(Long id) {
		Chauffeur c = r.findById(id).orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
		if (c.getExpirationPermis().isBefore(LocalDate.now())) throw new BusinessException("Permis expiré");
		c.setStatut(StatutChauffeur.DISPONIBLE);
		if (c.getVehicule() != null) c.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
		return ChauffeurMapper.toResponse(r.save(c));
	}

	public ChauffeurResponse suspendre(Long id) {
		Chauffeur c = r.findById(id).orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
		c.setStatut(StatutChauffeur.SUSPENDU);
		if (c.getVehicule() != null) c.getVehicule().setStatut(StatutVehicule.HORS_SERVICE);
		return ChauffeurMapper.toResponse(r.save(c));
	}
}
