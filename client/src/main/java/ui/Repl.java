package ui;

import java.util.Scanner;
import ui.EscapeSequences;

public class Repl {
    private final ChessClient client;
    private Scanner scanner;

    public Repl(String serverUrl) {
        scanner = new Scanner(System.in);
        client = new ChessClient(serverUrl, scanner);
    }

    public void run() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.println("Welcome to CHESS");
        System.out.println(client.help());

        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }
}
