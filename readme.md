# TodoApp

## Description

TodoApp est une application web permettant de gérer des tâches (Todos) de manière simple et sécurisée.  
Elle offre une interface utilisateur intuitive pour créer, afficher et organiser des todos, avec gestion des catégories et sécurité via authentification.

## Fonctionnalités principales

Gestion des Todos : création, lecture, mise à jour et suppression (CRUD)

Association des todos à des catégories avec couleur

Validation des formulaires côté serveur et côté client

Sécurisation des pages avec Spring Security

Logs techniques et logs d’audit pour suivre les actions des utilisateurs

## Technologies

- **Back-end :** Spring Boot 4, Kotlin, Spring Data JPA, H2 (base de données embarquée)
- **Front-end :** Thymeleaf
- **Sécurité :** Spring Security
- **Build :** Gradle Kotlin DSL
- **JDK :** 21

## Prérequis

- Java 21 installé sur votre machine
- Gradle (optionnel si vous utilisez le wrapper fourni `./gradlew`)
- Navigateur web moderne


## Installation

1. Cloner le dépôt :
   ```bash
   git clone <url_du_projet>
   cd TodoApp
   ```
2.  Construire le projet :
 ```bash
./gradlew build
 ```
3. Lancer l’application :
```bash
./gradlew bootRun
 ```
4. Accéder à l’application depuis votre navigateur :
   http://localhost:8080
5. Si le port 8080 est déjà utilisé, vous pouvez le modifier dans src/main/resources/application.properties avec :
```properties
 server.port=XXXX
```
## Structure du projet
```
src/
 ├─ main/
 │   ├─ kotlin/                  # Classes Kotlin (Controllers, Services, DAO)
 │   ├─ resources/
 │   │   ├─ templates/           # Templates Thymeleaf
 │   │   ├─ application.properties
 │   │   └─ static/              # CSS, JS, images
 └─ test/                        # Tests unitaires et d’intégration
```
