package gestio.moneders.moneders;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import gestio.moneders.moneders.dto.WalletDto;
import gestio.moneders.moneders.mapper.PersonaMapper;
import gestio.moneders.moneders.model.Persona;
import gestio.moneders.moneders.service.PersonaService;
import gestio.moneders.moneders.service.WalletService;
import gestio.moneders.moneders.settings.Settings;

@SpringBootApplication
public class MonedersApplication {
	private static final boolean CLI = true;
	private static final String MENU = """
================================================
								MONEDERS CLI
================================================

PERSONAS
	1  - List all personas
	2  - Find persona by ID
	3  - Create persona
	4  - Update persona
	5  - Delete persona

WALLETS
	6  - List all wallets
	7  - Find wallet by ID
	8  - Create wallet
	9  - Update wallet
	10 - Delete wallet

SYSTEM
 14  - Exit

------------------------------------------------
Choose an option:""";
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MonedersApplication.class, args);
		PersonaService personaService = context.getBean(PersonaService.class);
		PersonaMapper personaMapper = context.getBean(PersonaMapper.class);
		WalletService walletService = context.getBean(WalletService.class);
		

		if (CLI) {
			boolean running = true;
			int input;
			String xlsxPath = Path.of("C:\\Users\\AlbaSegura\\Documents\\moneders\\moneders\\prova.xlsx").toAbsolutePath().normalize().toString();
			Settings.setPath(xlsxPath);

			while (running) {
				System.out.println(MENU);
				input = ReadInput.readInputInt();
				switch (input) {
					case 1 -> personaService.findAll()
							.collectList()
							.block()
							.forEach(System.out::println);
					case 2 -> {
						System.out.println("Id persona:");
						var persona = personaService.findById(Long.valueOf(ReadInput.readInput())).block();
						if (persona != null) {
							System.out.println(persona);
						} else {
							print("Persona not found");
						}
					}
					case 3 -> {
						print("Nom:");
						String nom = ReadInput.readInput();
						print("Grup:");
						String grup = ReadInput.readInput();
						print("Membre Gestio (True/False):");
						String mg = ReadInput.readInput();
						boolean membreGestio = "true".equalsIgnoreCase(mg)
											|| "yes".equalsIgnoreCase(mg)
											|| "1".equals(mg);
						Persona p = new Persona(null, nom, grup, membreGestio);
						var persona = personaService.save(personaMapper.toDto(p)).block();
						if (persona != null) {
							System.out.println(persona);
						} else {
							print("Persona not found");
						}

					}
					case 4 -> {
						System.out.println("Id persona:");
						Long id = Long.valueOf(ReadInput.readInput());

						var persona = personaService.findById(id).block();
						
						if (persona != null) {
							print("Nom:");
							String nom = ReadInput.readInput();
							print("Grup:");
							String grup = ReadInput.readInput();
							print("Membre Gestio (True/False):");
							String mg = ReadInput.readInput();
							boolean membreGestio = "true".equalsIgnoreCase(mg)
												|| "yes".equalsIgnoreCase(mg)
												|| "1".equals(mg);
							Persona p = new Persona(id, nom, grup, membreGestio);
							var update = personaService.updateById(id, personaMapper.toDto(p)).block();
							if (update != null) {
								System.out.println(update);
							} else {
								print("Persona not found");
							}
						} else {
							print("Persona not found");
						}
					}
					case 5 -> {
						System.out.println("Id persona:");
						Long id = Long.valueOf(ReadInput.readInput());

						var persona = personaService.findById(id).block();
						if (persona != null) {
							personaService.deleteById(id).block();
							print("Persona deleted");
						} else {
							print("Persona not found");
						}
					}
					case 6 -> walletService.findAll()
								.collectList()
								.block()
								.forEach(w -> System.out.println(
										w.toDisplayString(personaId -> {
											if (personaId == null) return "N/A";
											var p = personaService.findById(personaId).block();
											return p != null ? p.getNom() : "Unknown"; 
										})
								));
					case 7 -> {
						System.out.println("Id wallet:");
						var wallet = walletService.findById(Long.valueOf(ReadInput.readInput())).block();
						if (wallet != null) {
							System.out.println(wallet.toDisplayString(personaId -> resolvePersonaName(personaService, personaId)));
						} else {
							print("Wallet not found");
						}
					}
					case 9 -> {
						System.out.println("Id wallet:");
						Long id = Long.valueOf(ReadInput.readInput());

						var current = walletService.findById(id).block();
						
						if (current != null) {
							print("Responsable gestio (persona id):");
							Long responsableGestio = Long.valueOf(ReadInput.readInput());

							print("Responsable (persona id):");
							Long responsable = Long.valueOf(ReadInput.readInput());

							print("Diners inicials:");
							Double dinersInicals = Double.valueOf(ReadInput.readInput());

							print("Diners justificats:");
							Double dinersJustificats = Double.valueOf(ReadInput.readInput());

							print("Diners finals:");
							Double dinersFinals = Double.valueOf(ReadInput.readInput());

							print("Data entrega (MM/dd/yyyy):");
							LocalDate dataEntrega = LocalDate.parse(ReadInput.readInput(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

							print("Data limit (MM/dd/yyyy):");
							LocalDate dataLimit = LocalDate.parse(ReadInput.readInput(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

							print("Concepte:");
							String concepte = ReadInput.readInput();

							print("Retornat (true/false):");
							String ret = ReadInput.readInput();
							boolean retornat = "true".equalsIgnoreCase(ret) || "yes".equalsIgnoreCase(ret) || "1".equals(ret);

							WalletDto wallet = new WalletDto(
									id, responsableGestio, responsable, dinersInicals, dinersJustificats,
									dinersFinals, dataEntrega, dataLimit, concepte, retornat
							);

							var updated = walletService.updateById(id, wallet).block();
							if (updated != null) {
								System.out.println(updated.toDisplayString(personaId -> resolvePersonaName(personaService, personaId)));
							} else {
								print("Wallet not found");
							}
						} else {
							print("Wallet not found");
						}
					}
					case 14 -> {
						running = false;
						SpringApplication.exit(context);
					}
					default -> print("Invalid option");
				}
			}
		}
	}


	public static void print(String s){
		System.out.println(s);
	}
	public static void print(int s){
		System.out.println(s);
	}public static void print(boolean s){
		System.out.println(s);
	}
	private static String resolvePersonaName(PersonaService walletService, Long walletId) {
		if (walletId == null) return "N/A";
		var p = walletService.findById(walletId).block();
		return p != null ? p.getNom() : "Unknown";
	}
}