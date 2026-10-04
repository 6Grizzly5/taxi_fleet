package com.sio2.taxiparking.service.impl;
import com.sio2.taxiparking.dto.request.VehiculeRequest;
import com.sio2.taxiparking.dto.response.VehiculeResponse;
import com.sio2.taxiparking.entity.Vehicule;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.exception.ResourceNotFoundException;
import com.sio2.taxiparking.mapper.VehiculeMapper;
import com.sio2.taxiparking.repository.VehiculeRepository;
import com.sio2.taxiparking.service.VehiculeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
@Service
public class VehiculeServiceImpl implements VehiculeService {
    private final VehiculeRepository repository;
    public VehiculeServiceImpl(VehiculeRepository repository) { this.repository = repository; }
    public VehiculeResponse ajouter(VehiculeRequest r) {
        Vehicule v = new Vehicule();
        v.setImmatriculation(r.immatriculation()); v.setMarque(r.marque()); v.setModele(r.modele());
        v.setAnnee(r.annee()); v.setCapacite(r.capacite()); v.setKilometrage(r.kilometrage());
        try { return VehiculeMapper.toResponse(repository.save(v)); }
        catch (Exception e) { throw new BusinessException("Immatriculation déjà utilisée"); }
    }
    public Page<VehiculeResponse> trouverTous(int page, int size, String sort, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByImmatriculationContainingIgnoreCase(search == null ? "" : search, pageable).map(VehiculeMapper::toResponse);
    }
    public VehiculeResponse trouverParId(Long id) {
        return VehiculeMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable")));
    }
}
