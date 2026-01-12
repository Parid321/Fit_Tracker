# Menaxhimi i Aktivitetit Fizik

## Përshkrimi
Ky projekt është një **aplikacion desktop i zhvilluar në gjuhën Java (Swing)** për menaxhimin e aktivitetit fizik dhe monitorimin e kalorive të djegura. Projekti është realizuar për qëllime akademike dhe synon të demonstrojë ndërtimin e një sistemi të strukturuar mirë, përdorimin e databazës lokale SQLite dhe aplikimin e testimit të automatizuar me JUnit.

Aplikacioni i lejon përdoruesuesit të regjistrojë aktivitetet fizike ditore, të llogarisë automatikisht kaloritë e djegura dhe të analizojë progresin në kohë.

---

## Funksionalitetet Kryesore
- Regjistrimi dhe autentikimi i përdoruesve
- Shtimi, modifikimi dhe fshirja e aktiviteteve fizike
- Llogaritja automatike e kalorive të djegura
- Filtrimi i aktiviteteve sipas intervaleve kohore (datë)
- Eksportimi i aktiviteteve në format CSV
- Ruajtja e të dhënave në databazë lokale SQLite

---

## Struktura e Projektit

```
src/
 ├── app/        → Klasa kryesore (Main – entry point)
 ├── db/         → Inicializimi i databazës dhe lidhja me SQLite
 ├── model/      → Entitetet kryesore (User, Activity, Statistics)
 ├── dao/        → Aksesi në databazë (UserDao, ActivityDao)
 ├── service/    → Logjika e biznesit (AuthService, CaloriesService)
 ├── util/       → Funksione ndihmëse (CSV Export)
 ├── ui/         → Ndërfaqja grafike e përdoruesit (Java Swing)
 └── tests/      → Testet unit (JUnit)
```

---

## Instalimi dhe Ekzekutimi

### Kërkesat
- Java JDK 17 ose më i ri
- Visual Studio Code, IntelliJ IDEA ose Eclipse
- SQLite (përmes SQLite JDBC Driver)
- JUnit 5 (për testim)
- JaCoCo (për code coverage)

### Hapat për Ekzekutim
1. Klono projektin nga GitHub:
   ```bash
   git clone https://github.com/USERNAME/REPOSITORY_NAME.git
   ```

2. Hape projektin në IDE-në e preferuar.

3. Sigurohu që `sqlite-jdbc` ndodhet në folderin `lib/`.

4. Ekzekuto aplikacionin nga klasa kryesore `Main`.

Databaza reale e aplikacionit (`fittrack.db`) krijohet automatikisht në root të projektit gjatë ekzekutimit të parë.

---

## Databaza
Aplikacioni përdor **SQLite**, një databazë lokale e bazuar në file.

- Databaza reale: `fittrack.db`
- Databaza për testim: `fittrack_test.db`

Databaza e testimit përdoret vetëm gjatë ekzekutimit të testeve JUnit dhe është e ndarë nga databaza reale për të shmangur ndikimin e testeve mbi të dhënat reale.

---

## Unit Testing

Testimi i aplikacionit është realizuar duke përdorur **JUnit 5**. Testet ndodhen në paketën:

```
src/tests/
```

### Testet e Implementuara
- `AuthServiceTest` – teston regjistrimin dhe autentikimin e përdoruesve
- `CaloriesServiceTest` – teston korrektësinë e llogaritjes së kalorive
- `ActivityFilterTest` – teston filtrimin e aktiviteteve sipas datës
- `CsvUtilTest` – teston eksportimin e të dhënave në format CSV

---

## Code Coverage

Për matjen e code coverage përdoret **JaCoCo**, në kombinim me testet JUnit.

- Coverage totale: rreth **25%**
- Branch coverage: rreth **31%**

Coverage përqendrohet në:
- Service layer
- DAO layer
- Utility classes

Ndërfaqja grafike (Java Swing UI) dhe klasa `Main` nuk janë të testuara, pasi testimi i GUI kërkon mjete të specializuara dhe nuk është pjesë e qëllimit të këtij projekti akademik.

---

## Arkitektura e Sistemit
Aplikacioni ndjek një **arkitekturë të tipit Layered**, e ndarë në:
- UI Layer – ndërfaqja grafike
- Service Layer – logjika e biznesit
- DAO Layer – akses në databazë
- Model Layer – përfaqësimi i të dhënave

---

## Versionimi i Kodit
Versionimi i kodit është realizuar duke përdorur **Git**, me commit-e të rregullta që pasqyrojnë fazat kryesore të zhvillimit, testimit dhe dokumentimit.

---

## Përfundim
Ky projekt përmbush kërkesat funksionale dhe jo-funksionale të përcaktuara në detyrë. Ai demonstron zhvillimin e një aplikacioni desktop në Java, përdorimin e databazës SQLite, ndarjen e qartë të përgjegjësive dhe implementimin e testimit të automatizuar me code coverage.

Projekti është i përshtatshëm për qëllime akademike dhe përfaqëson një bazë të qëndrueshme për zgjerime të ardhshme.
