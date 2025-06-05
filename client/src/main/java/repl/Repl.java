package repl;

import client.*;
import websocket.NotificationHandler;
import websocket.messages.NotificationMessage;
import java.util.Scanner;

public class Repl implements NotificationHandler {

    public Client client;

    public Repl(String serverUrl) {
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

    public void notify(NotificationMessage notification){
        System.out.println(notification.getMessage());
        System.out.print("\n>>> ");
    }

}
