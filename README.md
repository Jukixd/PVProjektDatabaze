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
3.  Ujistěte se, že údaje odpovídají (pokud jste použili SQL skript výše, nemusíte nic měnit):
    ```properties
    db.url=jdbc:mysql://localhost:3306/board_game_cafe?allowPublicKeyRetrieval=true&useSSL=false
    db.user=bg_user
    db.password=bg_pass
    ```

### 3. Spuštění
Spusťte hlavní třídu aplikace: `src/boardgamecafe/Main.java`.

---

### Importování CSV souborů

Všechny CSV soubory, které chcete importovat, musíte nejdříve umístit do složky **`data`** v kořenovém adresáři projektu. Import jinak neprojde.

Dále je důležité dodržovat určitou strukturu dat v souboru. Níže je specifikovaná struktura CSV souborů pro import do tabulek `customer` a `game`, kde XXX znázorňuje vaše vlastní data:

**customers.csv**

* email musí být unikátní
* Pořadí: Jméno, Email, Telefon

Fragment kódu
```
Petr Novotny,petr@test.cz,777111222
```
**games.csv**

* genre musí být jedna z hodnot: STRATEGIE, RODINNA, PARTY, RPG
* price musí být desetinné číslo (tečka jako oddělovač)
* Pořadí: Název, Žánr, Cena

Fragment kódu
```
Carcassonne,STRATEGIE,80.0
```