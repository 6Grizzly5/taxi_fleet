package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.InscriptionRequest;
import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.enumeration.RoleUtilisateur;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.repository.UtilisateurRepository;
import com.sio2.taxiparking.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(UtilisateurRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur inscrire(InscriptionRequest request) {
        String email = request.email().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new BusinessException("Cette adresse email est déjà utilisée");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.nom().trim());
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        utilisateur.setRole(RoleUtilisateur.USER);
        utilisateur.setActif(true);

        return repository.save(utilisateur);
    }
}
