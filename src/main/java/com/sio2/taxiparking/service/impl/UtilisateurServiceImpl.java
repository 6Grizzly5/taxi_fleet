package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.*;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.BusinessException;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	private final UtilisateurRepository ur;
	private final ClientRepository cr;
	private final ChauffeurRepository chr;
	private final VehiculeRepository vr;
	private final PasswordEncoder pe;

	public UtilisateurServiceImpl(
			UtilisateurRepository ur,
			ClientRepository cr,
			ChauffeurRepository chr,
			VehiculeRepository vr,
			PasswordEncoder pe) {
		this.ur = ur;
		this.cr = cr;
		this.chr = chr;
		this.vr = vr;
		this.pe = pe;
	}

	private String email(String e) {
		return e.trim().toLowerCase();
	}

	private void unique(String e) {
		if (ur.existsByEmail(e)) throw new BusinessException("Cette adresse email est déjà utilisée");
	}

	@Transactional
	public Utilisateur inscrireClient(InscriptionRequest r) {
		String e = email(r.email());
		unique(e);
		Utilisateur u = new Utilisateur();
		u.setNom(r.nom().trim());
		u.setEmail(e);
		u.setMotDePasse(pe.encode(r.motDePasse()));
		u.setRole(RoleUtilisateur.CLIENT);
		u.setActif(true);
		u = ur.save(u);
		Client c = new Client();
		c.setNom(u.getNom());
		c.setTelephone(r.telephone().trim());
		c.setEmail(e);
		c.setUtilisateur(u);
		cr.save(c);
		return u;
	}

	@Transactional
	public Utilisateur inscrireChauffeur(ChauffeurInscriptionRequest r) {
		String e = email(r.email());
		unique(e);
		String imm = r.immatriculation().trim().toUpperCase();
		if (vr.existsByImmatriculation(imm)) throw new BusinessException("Cette immatriculation est déjà utilisée");
		if (chr.existsByNumeroPermis(r.numeroPermis().trim())) throw new BusinessException("Ce permis est déjà utilisé");
		Utilisateur u = new Utilisateur();
		u.setNom(r.nom().trim());
		u.setEmail(e);
		u.setMotDePasse(pe.encode(r.motDePasse()));
		u.setRole(RoleUtilisateur.CHAUFFEUR);
		u.setActif(true);
		u = ur.save(u);
		Vehicule v = new Vehicule();
		v.setImmatriculation(imm);
		v.setMarque(r.marque().trim());
		v.setModele(r.modele().trim());
		v.setAnnee(r.annee());
		v.setCapacite(r.capacite());
		v.setKilometrage(0.0);
		v.setStatut(StatutVehicule.DISPONIBLE);
		v = vr.save(v);
		Chauffeur c = new Chauffeur();
		c.setNom(u.getNom());
		c.setTelephone(r.telephone().trim());
		c.setNumeroPermis(r.numeroPermis().trim());
		c.setExpirationPermis(r.expirationPermis());
		c.setDateEmbauche(LocalDate.now());
		c.setStatut(StatutChauffeur.INDISPONIBLE);
		c.setVehicule(v);
		c.setUtilisateur(u);
		chr.save(c);
		return u;
	}
}
