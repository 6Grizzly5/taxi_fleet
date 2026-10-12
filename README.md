# TaxiParking - Spring Boot

Projet Framework Java SIO 2026 - gestion de flotte de taxis + parking.

## Technologies
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Data JPA / Hibernate
- MySQL
- Spring Security / BCrypt
- DTO + validation + mapper
- API REST
- Transactions métier

## Lancement
1. Créer/ouvrir MySQL.
2. Vérifier `src/main/resources/application.properties`.
3. Depuis la racine:
   - Windows: `mvnw.cmd spring-boot:run`
   - ou `mvnw.cmd clean package` puis `java -jar target/taxiparking-0.0.1-SNAPSHOT.jar`
4. Ouvrir `http://localhost:8080/login`.

## Comptes
Admin créé automatiquement au démarrage:
- Email: `admintaxi@gmail.com`
- Mot de passe: `123admin`

Les clients et chauffeurs s'inscrivent depuis l'interface.

## Scénario de démonstration
1. Inscrire un chauffeur.
2. Se connecter en admin et valider le chauffeur.
3. Se déconnecter.
4. Inscrire un client et se connecter.
5. Commander un taxi.
6. Se déconnecter puis se connecter avec le chauffeur.
7. Accepter la course, la démarrer puis la terminer.
8. Revenir côté client pour constater l'état de la course.
9. Tester le module parking depuis le compte client.

## API REST
- `GET/POST /api/clients`
- `GET/POST /api/drivers`
- `GET/POST /api/vehicles`
- `GET/POST /api/rides`
- `POST /api/rides/{id}/assign`
- `POST /api/rides/{id}/cancel`

Les listes utilisent pagination, tri et recherche lorsque pertinent.


## Finalisation interface
- Bouton « Retour » dans la barre supérieure des pages.
- Page d'erreur conviviale pour les routes inexistantes, accès refusés et erreurs serveur.
- Liens internes générés avec Thymeleaf afin de respecter le contexte de l'application.
- Espace d'administration du parking: `/admin/parking`.
- Annulation d'une réservation depuis l'espace client.

## Pages principales
- Client: `/client/dashboard`, `/client/course/nouveau`, `/client/parking`
- Chauffeur: `/chauffeur/dashboard`
- Administration: `/admin/dashboard`, `/admin/parking`, `/clients`, `/chauffeurs`, `/vehicules`, `/courses`

En cas d'erreur technique inattendue, le navigateur affiche une page explicative. Les détails techniques restent dans les journaux du serveur.
