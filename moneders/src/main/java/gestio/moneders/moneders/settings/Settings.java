package gestio.moneders.moneders.settings;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gestio.moneders.moneders.ReadInput;

public final class Settings {
    private static volatile String path;

    private Settings() {
    }

    public static void setPath(String p) {
        path = p;
    }

    public static String getPath() {
        return path;
    }

    public static String setNewPath() {
        boolean excel = false;
        String input = "";
        while (!excel) {
            System.out.println("Ruta del fitxer xlsx:");
            input = ReadInput.readInput();
            excel = isValidExcelFile(input);
            if (!excel) {
                System.out.println("Invalid Excel file. Try again.");
            }
        }
        return input;
    }

    public static boolean isValidExcelFile(String input) {
        try {
            Path path = Paths.get(input);
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                return false;
            }
            String fileName = path.getFileName().toString().toLowerCase();
            return fileName.endsWith(".xlsx") || fileName.endsWith(".xls");
            
        } catch (InvalidPathException e) {
            return false;
        }
    }
}
