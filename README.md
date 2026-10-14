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


## Attribution automatique et statistiques

L'attribution automatique est déclenchée dès qu'un client crée une course, depuis l'interface Thymeleaf ou l'API REST. Le système choisit un chauffeur DISPONIBLE dont le permis est valide, associé à un véhicule DISPONIBLE et assez grand pour la course. Le statut passe alors de EN_ATTENTE à ASSIGNEE : le chauffeur n'a pas à accepter la course, il peut directement la démarrer depuis son tableau de bord. Le clic sur « Démarrer » fait passer la course à EN_COURS ; « Terminer » clôt la course et libère le chauffeur et le véhicule. Si aucun chauffeur n'est éligible, la course reste EN_ATTENTE. L'administrateur peut aussi relancer l'attribution avec `POST /api/rides/{id}/assign`. Le score minimise d'abord le nombre de courses terminées, puis le kilométrage du véhicule. Les coefficients sont configurables dans `application.properties` (`taxi.scoring.course-count-weight`, `taxi.scoring.kilometrage-weight`).

Le cycle de vie REST inclut `POST /api/rides/{id}/start` et `POST /api/rides/{id}/complete`. Ces deux routes identifient le chauffeur depuis le compte authentifié par HTTP Basic et n'acceptent que la course qui lui est attribuée.

Les statistiques sont disponibles sur `GET /api/statistics/revenue` et `GET /api/statistics/activity`, avec `from=YYYY-MM-DD` et `to=YYYY-MM-DD` facultatifs. Par défaut, la période couvre le début de l'année jusqu'à aujourd'hui. Le chiffre d'affaires ne compte que les courses TERMINEE. Le taux de commission est configurable avec `taxi.commission-rate` (0.10 = 10 %). Les revenus par chauffeur et véhicule sont calculés à partir des courses terminées et les revenus chauffeurs correspondent au chiffre d'affaires moins la commission de l'entreprise.

Exemple : `GET http://localhost:8080/api/statistics/revenue?from=2026-01-01&to=2026-12-31`. Les routes `/api/statistics/**`, l'attribution et l'annulation sont réservées au rôle ADMIN. Les routes REST protégées utilisent la configuration Spring Security du projet.


## Vérifications rapides avant la soutenance

1. Démarrer MySQL et créer (ou laisser Spring créer) la base `taxi_fleet`.
2. Vérifier `src/main/resources/application.properties` et adapter l'identifiant/mot de passe MySQL si nécessaire.
3. Avec Java 17+, lancer `mvn clean package` puis `mvn spring-boot:run` (ou utiliser le wrapper Windows `mvnw.cmd`).
4. Se connecter à l'interface Web avec le compte administrateur initialisé par l'application : `admintaxi@gmail.com` / `123admin`. Changez ce mot de passe avant tout usage réel.
5. Importer `postman/TaxiFleet.postman_collection.json` dans Postman. L’API REST utilise HTTP Basic : les identifiants sont configurés dans les variables de la collection. La protection CSRF reste activée pour l’interface Thymeleaf, tandis que la chaîne API est stateless. Utilisez HTTPS en dehors du développement local.
6. Pour tester l'attribution automatique, créer au moins un client, un véhicule disponible et un chauffeur avec permis non expiré, associer le véhicule au chauffeur, puis vérifier que le chauffeur est DISPONIBLE et que le compte est validé. Créer ensuite une course dont le nombre de passagers ne dépasse pas la capacité du véhicule. La course doit immédiatement passer à ASSIGNEE et apparaître dans le tableau de bord du chauffeur, sans bouton « Accepter ». Le chauffeur clique sur « Démarrer » pour passer à EN_COURS, puis sur « Terminer » pour clôturer la course.
7. Terminer une course avant de vérifier les revenus : seules les courses `TERMINEE` contribuent au chiffre d'affaires. Le kilométrage de la course doit être renseigné.

Les endpoints de statistiques renvoient une synthèse structurée : `GET /api/statistics/revenue?from=2026-01-01&to=2026-12-31` et `GET /api/statistics/activity?from=2026-01-01&to=2026-12-31`. Les dates sont inclusives. La commission par défaut est de 10 %, modifiable via `taxi.commission-rate`.
