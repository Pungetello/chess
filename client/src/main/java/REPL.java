import client.*;

import java.util.Scanner;

public class REPL {

    private Client client;

    public REPL(String serverUrl) {
        client = new LoggedOutClient(serverUrl, this);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>> ");
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


}
