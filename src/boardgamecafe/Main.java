package boardgamecafe;

import boardgamecafe.entity.Game;
import boardgamecafe.entity.GameGenre;
import boardgamecafe.repository.GameRepository;
import boardgamecafe.service.RentalService;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameRepository gameRepo = new GameRepository();

        Game h1 = new Game("Dixit", GameGenre.PARTY, 100.0f, true);
        Game h2 = new Game("Dungeons & Dragons", GameGenre.RPG, 200.0f, true);

        Integer idHry1 = gameRepo.save(h1);
        Integer idHry2 = gameRepo.save(h2);

        System.out.println("Připraveny hry ID: " + idHry1 + " a " + idHry2);

        RentalService service = new RentalService();

        System.out.println("--- Spouštím transakci výpůjčky ---");
        List<Integer> hryKPujceni = Arrays.asList(idHry1, idHry2);

        boolean vysledek = service.createRental(1, hryKPujceni);

        if (vysledek) {
            System.out.println("Vyslo to chlape");
        } else {
            System.out.println("neco selhalo.");
        }
    }
}