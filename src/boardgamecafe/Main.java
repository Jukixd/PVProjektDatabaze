package boardgamecafe;
import boardgamecafe.entity.Customer;
import boardgamecafe.repository.CustomerRepository;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        CustomerRepository customerRepo = new CustomerRepository();
        System.out.println("--- TEST: Vložení zákazníka ---");
        Customer novyZakaznik = new Customer("Jan Novak", "jan.novak@email.cz", "123456789");
        Integer newId = customerRepo.save(novyZakaznik);
        System.out.println("Zákazník uložen s ID: " + newId);
        System.out.println("\n--- TEST: Najít podle ID ---");
        Customer nacteny = customerRepo.findById(newId);
        System.out.println("Načteno z DB: " + nacteny);
        System.out.println("\n--- TEST: Všechny zákazníky ---");
        List<Customer> all = customerRepo.findAll();
        for (Customer c : all) {
            System.out.println(c);
        }
    }
}