# Board Game Cafe Manager

Konzolová aplikace pro správu deskovkářské kavárny implementovaná v jazyce **Java** s využitím relační databáze **MySQL**. Aplikace využívá návrhový vzor **Repository (D1)**, Transakce a Singleton.

## Software požadavky

Aplikace vyžaduje pro svůj běh následující software:
* **Java JDK 17+**
* **MySQL Server 8.0+**
* **MySQL Connector/J** (Ovladač je součástí projektu)

## Instalace a spuštění aplikace

### 1. Vytvoření a nastavení databáze
1.  Připojte se k databázovému serveru
2.  Otevřete a spusťte SQL skript umístěný v:
    `script.sql`
3.  Tento skript automaticky:
    * Vytvoří databázi `board_game_cafe`.
    * Vytvoří uživatele `bg_user` s heslem `bg_pass`.
    * Vytvoří tabulky a potřebné pohledy (Views).

### 2. Nastavení konfiguračního souboru
1.  Přejděte do složky `src`.
2.  Otevřete soubor `config.properties`.
3.  Ujistěte se, že údaje odpovídají:
    ```properties
    db.url=jdbc:mysql://localhost:3306/board_game_cafe?allowPublicKeyRetrieval=true&useSSL=false
    db.user= váš username
    db.password= váš password
    ```

### 3. Spuštění
Otevřere terminál ve c kořenovém adresáři a spusťte aplikaci příkazem:

!! Nepřesunujte BGcafe.jar z jeho aktulaní polohy !!

```cmd
    cd C:\umístění\složky\souboru\BGcafe.jar
    
    java -jar BGcafe.jar
```

---

### Importování CSV souborů

Pro import dat je nutné mít ve složce data připravené CSV soubory.

Důležité: Aplikace je naprogramována tak, aby automaticky hledala pouze soubory s těmito přesnými názvy:

* customers.csv
* games.csv
* tables.csv

Pokud soubory pojmenujete jinak, import neproběhne. Dbejte také na správnou strukturu a oddělovače:

**games.csv**
* email musí být unikátní
* Pořadí: Jméno, Email, Telefon

Fragment kódu
```
Petr Novotny,petr@test.cz,777111222
Jan Novak,jan@novak.cz,608123456
```
**games.csv**

* genre musí být jedna z hodnot: STRATEGIE, RODINNA, PARTY, RPG
* price musí být desetinné číslo (tečka jako oddělovač)
* Pořadí: Název, Žánr, Cena

Fragment kódu
```
Carcassonne,STRATEGIE,80.0
Krycí jména,PARTY,45.0
```
**tables.csv**

* Slouží k definici kapacity a popisu stolů.
* Pořadí: Kapacita; Popis

Fragment kódu
```
4;U okna (Výhled na náměstí)
2;Romantický box
6;Velký stůl pro D&D
```
---
### Testování Aplikace
Testovací scénáře a dokumentace se nachazí ve složce /Doc 