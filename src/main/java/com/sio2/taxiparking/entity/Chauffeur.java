package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.StatutChauffeur;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "chauffeurs")
public class Chauffeur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nom;

	@Column(nullable = false)
	private String telephone;

	@Column(name = "numero_permis", nullable = false, unique = true)
	private String numeroPermis;

	@Column(name = "expiration_permis", nullable = false)
	private LocalDate expirationPermis;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatutChauffeur statut = StatutChauffeur.INDISPONIBLE;

	@Column(name = "date_embauche", nullable = false)
	private LocalDate dateEmbauche;

	@OneToOne
	@JoinColumn(name = "vehicule_id")
	private Vehicule vehicule;

	@OneToOne
	@JoinColumn(name = "utilisateur_id", unique = true)
	private Utilisateur utilisateur;

	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String v) {
		nom = v;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String v) {
		telephone = v;
	}

	public String getNumeroPermis() {
		return numeroPermis;
	}

	public void setNumeroPermis(String v) {
		numeroPermis = v;
	}

	public LocalDate getExpirationPermis() {
		return expirationPermis;
	}

	public void setExpirationPermis(LocalDate v) {
		expirationPermis = v;
	}

	public StatutChauffeur getStatut() {
		return statut;
	}

	public void setStatut(StatutChauffeur v) {
		statut = v;
	}

	public LocalDate getDateEmbauche() {
		return dateEmbauche;
	}

	public void setDateEmbauche(LocalDate v) {
		dateEmbauche = v;
	}

	public Vehicule getVehicule() {
		return vehicule;
	}

	public void setVehicule(Vehicule v) {
		vehicule = v;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur v) {
		utilisateur = v;
	}
}
