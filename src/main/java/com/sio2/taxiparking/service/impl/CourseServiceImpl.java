package com.sio2.taxiparking.service.impl;

import com.sio2.taxiparking.dto.request.CourseRequest;
import com.sio2.taxiparking.dto.response.CourseResponse;
import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.exception.*;
import com.sio2.taxiparking.mapper.CourseMapper;
import com.sio2.taxiparking.repository.*;
import com.sio2.taxiparking.service.CourseService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class CourseServiceImpl implements CourseService {

	private final CourseRepository r;
	private final ClientRepository cr;
	private final ChauffeurRepository chr;

	public CourseServiceImpl(CourseRepository r, ClientRepository cr, ChauffeurRepository chr) {
		this.r = r;
		this.cr = cr;
		this.chr = chr;
	}

	private Course create(Client c, CourseRequest q) {
		Course x = new Course();
		x.setClient(c);
		x.setDepart(q.depart().trim());
		x.setDestination(q.destination().trim());
		x.setNombrePersonnes(q.nombrePersonnes());
		x.setTarif(q.tarif());
		x.setDistance(0.0);
		x.setDateHeure(java.time.LocalDateTime.now());
		x.setStatut(StatutCourse.EN_ATTENTE);
		return x;
	}

	public CourseResponse ajouter(CourseRequest q) {
		if (q.clientId() == null) throw new BusinessException("Le client est obligatoire");
		return ajouterPourClient(q.clientId(), q);
	}

	public CourseResponse ajouterPourClient(Long id, CourseRequest q) {
		Client c = cr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
		return CourseMapper.toResponse(r.save(create(c, q)));
	}

	public Page<CourseResponse> trouverTous(int p, int s, String sort, String search) {
		Pageable x = PageRequest.of(Math.max(0, p), Math.max(1, s), Sort.by(sort == null ? "dateHeure" : sort).descending());
		String q = search == null ? "" : search.trim();
		return r.findByDepartContainingIgnoreCaseOrDestinationContainingIgnoreCase(q, q, x)
				.map(CourseMapper::toResponse);
	}

	public Page<CourseResponse> pourClient(Long id, int p, int s) {
		return r.findByClientId(id, PageRequest.of(p, s, Sort.by("dateHeure").descending()))
				.map(CourseMapper::toResponse);
	}

	public Page<CourseResponse> pourChauffeur(Long id, int p, int s) {
		return r.findByChauffeurId(id, PageRequest.of(p, s, Sort.by("dateHeure").descending()))
				.map(CourseMapper::toResponse);
	}

	@Transactional
	public CourseResponse attribuer(Long id) {
		Course c = get(id);
		if (c.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("Cette course n'est plus en attente");
		return assign(c, null);
	}

	private CourseResponse assign(Course c, Chauffeur forced) {
		Chauffeur best = forced;
		if (best == null) {
			best = chr.findByStatut(StatutChauffeur.DISPONIBLE).stream()
					.filter(d -> d.getExpirationPermis().isAfter(LocalDate.now().minusDays(1)))
					.filter(d -> d.getVehicule() != null)
					.filter(d -> d.getVehicule().getStatut() == StatutVehicule.DISPONIBLE)
					.filter(d -> d.getVehicule().getCapacite() >= c.getNombrePersonnes())
					.min(java.util.Comparator.comparingDouble(
							d -> r.countByChauffeurId(d.getId()) * 1000.0 + d.getVehicule().getKilometrage()))
					.orElseThrow(() -> new BusinessException("Aucun chauffeur disponible pour cette course"));
		}
		if (best.getStatut() != StatutChauffeur.DISPONIBLE) {
			throw new BusinessException("Ce chauffeur n'est plus disponible");
		}
		if (best.getExpirationPermis() == null || best.getExpirationPermis().isBefore(LocalDate.now())) {
			throw new BusinessException("Le permis de ce chauffeur est expiré ou invalide");
		}
		if (best.getVehicule() == null || best.getVehicule().getCapacite() < c.getNombrePersonnes()) {
			throw new BusinessException("Véhicule incompatible avec le nombre de passagers");
		}
		if (best.getVehicule().getStatut() != StatutVehicule.DISPONIBLE) {
			throw new BusinessException("Le véhicule de ce chauffeur n'est pas disponible");
		}
		c.setChauffeur(best);
		c.setVehicule(best.getVehicule());
		c.setStatut(StatutCourse.ASSIGNEE);
		best.setStatut(StatutChauffeur.EN_COURSE);
		best.getVehicule().setStatut(StatutVehicule.EN_COURSE);
		chr.save(best);
		return CourseMapper.toResponse(r.save(c));
	}

	@Transactional
	public CourseResponse accepter(Long id, Long chauffeurId) {
		Course c = get(id);
		if (c.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("Course déjà traitée");
		Chauffeur d = chr.findById(chauffeurId)
				.orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
		if (d.getStatut() != StatutChauffeur.DISPONIBLE) throw new BusinessException("Chauffeur non disponible");
		return assign(c, d);
	}

	@Transactional
	public CourseResponse refuser(Long id, Long chauffeurId) {
		Course c = get(id);
		if (c.getStatut() != StatutCourse.EN_ATTENTE) throw new BusinessException("Course déjà traitée");
		c.setChauffeur(null);
		c.setVehicule(null);
		c.setStatut(StatutCourse.EN_ATTENTE);
		return CourseMapper.toResponse(r.save(c));
	}

	@Transactional
	public CourseResponse demarrer(Long id, Long chauffeurId) {
		Course c = get(id);
		if (c.getChauffeur() == null || !c.getChauffeur().getId().equals(chauffeurId)) {
			throw new BusinessException("Cette course ne vous appartient pas");
		}
		if (c.getStatut() != StatutCourse.ASSIGNEE) throw new BusinessException("La course doit être assignée");
		c.setStatut(StatutCourse.EN_COURS);
		return CourseMapper.toResponse(r.save(c));
	}

	@Transactional
	public CourseResponse terminer(Long id, Long chauffeurId) {
		Course c = get(id);
		if (c.getChauffeur() == null || !c.getChauffeur().getId().equals(chauffeurId)) {
			throw new BusinessException("Cette course ne vous appartient pas");
		}
		if (c.getStatut() != StatutCourse.EN_COURS) throw new BusinessException("La course doit être en cours");
		c.setStatut(StatutCourse.TERMINEE);
		Chauffeur d = c.getChauffeur();
		d.setStatut(StatutChauffeur.DISPONIBLE);
		if (d.getVehicule() != null) {
			d.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
			d.getVehicule().setKilometrage(d.getVehicule().getKilometrage() + c.getDistance());
		}
		chr.save(d);
		return CourseMapper.toResponse(r.save(c));
	}

	@Transactional
	public CourseResponse annuler(Long id) {
		Course c = get(id);
		if (c.getStatut() == StatutCourse.TERMINEE) throw new BusinessException("Course terminée impossible à annuler");
		if (c.getChauffeur() != null) {
			c.getChauffeur().setStatut(StatutChauffeur.DISPONIBLE);
			if (c.getChauffeur().getVehicule() != null) {
				c.getChauffeur().getVehicule().setStatut(StatutVehicule.DISPONIBLE);
			}
			chr.save(c.getChauffeur());
		}
		c.setStatut(StatutCourse.ANNULEE);
		return CourseMapper.toResponse(r.save(c));
	}

	private Course get(Long id) {
		return r.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course introuvable"));
	}
}
