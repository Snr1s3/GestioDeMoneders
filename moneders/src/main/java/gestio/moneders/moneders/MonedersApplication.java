package gestio.moneders.moneders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonedersApplication {
	private static final String MENU = "Opcions:\n1-Create\n2-Read\n3-Exit";
	public static void main(String[] args) {
		SpringApplication.run(MonedersApplication.class, args);
		String path = ".\\prova.xls";
		int option = 0;
		int exit = 3;
		while (option != exit){
			System.out.println(MENU);
			option = ReadInput.readInputInt();
			switch (option) {
				case 1 -> Excel.create(path);
				case 2 -> Excel.read(path);
				case 3 -> { }
				default -> System.out.println("Opció no vàlida");
			}
		}
	}
}