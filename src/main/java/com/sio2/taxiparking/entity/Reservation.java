package com.sio2.taxiparking.entity;

import com.sio2.taxiparking.enumeration.StatutReservation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private Utilisateur utilisateur;

	@ManyToOne(optional = false)
	private Vehicule vehicule;

	@ManyToOne(optional = false)
	private Place place;

	@Column(nullable = false)
	private LocalDateTime arriveePrevue;

	@Column(nullable = false)
	private LocalDateTime departPrevu;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatutReservation statut = StatutReservation.EN_ATTENTE;

	@Column(nullable = false)
	private Double tarif = 0.0;

	public Long getId() {
		return id;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur v) {
		utilisateur = v;
	}

	public Vehicule getVehicule() {
		return vehicule;
	}

	public void setVehicule(Vehicule v) {
		vehicule = v;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place v) {
		place = v;
	}

	public LocalDateTime getArriveePrevue() {
		return arriveePrevue;
	}

	public void setArriveePrevue(LocalDateTime v) {
		arriveePrevue = v;
	}

	public LocalDateTime getDepartPrevu() {
		return departPrevu;
	}

	public void setDepartPrevu(LocalDateTime v) {
		departPrevu = v;
	}

	public StatutReservation getStatut() {
		return statut;
	}

	public void setStatut(StatutReservation v) {
		statut = v;
	}

	public Double getTarif() {
		return tarif;
	}

	public void setTarif(Double v) {
		tarif = v;
	}
}
