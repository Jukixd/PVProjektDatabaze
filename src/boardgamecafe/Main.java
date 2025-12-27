package boardgamecafe;
import boardgamecafe.entity.CafeTable;
import boardgamecafe.entity.Game;
import boardgamecafe.entity.GameGenre;
import boardgamecafe.repository.CafeTableRepository;
import boardgamecafe.repository.GameRepository;
public class Main {
    public static void main(String[] args) {
        GameRepository gameRepo = new GameRepository();
        Game novaHra = new Game("Osadníci z Katanu", GameGenre.STRATEGIE, 50.0f, true);
        gameRepo.save(novaHra);
        System.out.println("Všechny hry v DB: " + gameRepo.findAll());


        CafeTableRepository tableRepo = new CafeTableRepository();
        tableRepo.save(new CafeTable(4, "U okna vlevo"));
        System.out.println("Všechny stoly v DB: " + tableRepo.findAll());
    }
}