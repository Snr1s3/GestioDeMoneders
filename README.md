# Gestió de Moneders

Aplicació de gestió de moneders i persones construïda amb Spring Boot. Permet gestionar personas, carteretes (wallets) i les seves transaccions a través d'una interfície de command-line (CLI) i fitxers Excel.

## Característiques

- **Gestió de Personas**: Crear, llistar, cercar, actualitzar i eliminar personas
- **Gestió de Carteretes**: Gestionar carteretes amb responsables, conceptes i dates
- **Integració Excel**: Lectura i escriptura de datos en fitxers XLSX
- **Interfície CLI**: Menú interactiu per accedir a totes les funcionalitats
- **Validació de Fitxers**: Validació de rutes i fitxers Excel

## Estructura del Projecte

```
src/main/java/gestio/moneders/moneders/
├── controller/          # Controladors REST
├── dto/                 # Data Transfer Objects
├── mapper/              # Mappers per a entitats
├── model/               # Entitats JPA
├── repository/          # Repositoris de dades
├── service/             # Lògica de negoci
└── settings/            # Configuració
```

## Tecnologies

- **Java 21**
- **Spring Boot 3.x**
- **Apache POI** (Excel)
- **Maven**

## Instal·lació

### Requisits

- Java 21+
- Maven 3.6+

### Instal·lació de Java 21

En Linux (Debian/Ubuntu):
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

### Construcció del Projecte

```bash
cd moneders
./mvnw clean install
```

## Ús

### Executar l'Aplicació

```bash
./mvnw spring-boot:run
```

L'aplicació t'esperarà per introduir la ruta del fitxer Excel:

```
Ruta del fitxer xlsx:
```

Introdueix la ruta absoluta o relativa del teu fitxer Excel (p.ex: `./prova.xlsx`)

### Menú CLI

```
PERSONAS
  1 - Llistar totes les personas
  2 - Cercar persona per ID
  3 - Crear persona
  4 - Actualitzar persona
  5 - Eliminar persona

CARTERETES
  6 - Llistar totes les carteretes
  7 - Cercar cartereta per ID
  8 - Crear cartereta
  9 - Actualitzar cartereta
  10 - Eliminar cartereta

CONFIGURACIÓ
  11 - Establir ruta del fitxer Excel

SISTEMA
  14 - Sortir
```

## Validació de Rutes

L'aplicació validarà que:
- La ruta existeixi al sistema de fitxers
- Sigui un fitxer (no un directori)
- Tingui extensió `.xlsx` o `.xls`

Si introdueixes una ruta relativa, es resoldrà desde el directori de treball actual de l'aplicació.

## Construcció d'un JAR Executable

```bash
./mvnw clean package
java -jar target/moneders-0.0.1-SNAPSHOT.jar
```

## Configuració

Edita `src/main/resources/application.properties` per personalitzar:
- Port de l'aplicació
- Conexió a base de dades
- Altres paràmetres de Spring Boot

## Informació de Contacte

Gestió de Moneders - Aplicació per a la gestió de carteretes i personas