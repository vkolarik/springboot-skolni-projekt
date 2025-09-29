# Music Streaming Analytics Platform

Spring Boot REST API pro analýzu hudebního streamingu s pokročilou statistickou analýzou.

## Funkcionalita

- Správa uživatelů s demografickými daty
- Katalog skladeb s audio vlastnostmi (danceability, energy, valence)
- Systém hodnocení a komentářů
- Analytika popularity, trendů a uživatelských preferencí

## Technologie

- Java 17, Spring Boot 3.2.2, PostgreSQL
- Spring Data JPA, Liquibase, OpenAPI/Swagger
- JUnit 5, REST Assured pro testování

## Architektura

Layered architecture s RESTful API designem. Databáze obsahuje tři hlavní entity: Users, Tracks, Favorites s normalizovaným schématem a foreign key constraints.

## Spuštění

1. Java 17, PostgreSQL
2. `./gradlew bootRun`
3. API dokumentace: http://localhost:8090/swagger-ui.html
4. Automatický import Spotify datasetu při startu

