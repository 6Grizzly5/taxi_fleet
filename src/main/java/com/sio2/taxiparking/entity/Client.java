package com.sio2.taxiparking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nom;

	@Column(nullable = false)
	private String telephone;

	@Column(nullable = false, unique = true)
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		email = v;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur v) {
		utilisateur = v;
	}
}
