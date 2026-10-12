package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.RoleUtilisateur;
import jakarta.persistence.*;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nom;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String motDePasse;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleUtilisateur role;

	@Column(nullable = false)
	private boolean actif = true;

	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String v) {
		nom = v;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		email = v;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String v) {
		motDePasse = v;
	}

	public RoleUtilisateur getRole() {
		return role;
	}

	public void setRole(RoleUtilisateur v) {
		role = v;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean v) {
		actif = v;
	}
}
