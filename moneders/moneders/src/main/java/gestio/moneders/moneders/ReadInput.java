package gestio.moneders.moneders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadInput {
    public static String readInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    public static int readInputInt() {
        String input = readInput();
        while (input == null || !input.trim().matches("-?\\d+")) {
            System.out.println("Please enter a valid integer:");
            input = readInput();
        }
        return Integer.parseInt(input.trim());
    }
}
