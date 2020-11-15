package ua.kiev.prog;

import ua.kiev.prog.com.gmail.psyh2409.Manager;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Manager manager = new Manager(scanner);
            manager.welcomeToHellp();
            manager.signup();
            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();
            while (true) {
                System.out.println("Enter your message: ");
                String text = scanner.nextLine();
                if (text.isEmpty()) break;
                if (manager.catchCommand(text)) {
                    String to = "everyone";
                    String message = "";
                    if (text.contains(":")) {
                        to = text.split(":")[0];
                        message = text.substring(text.indexOf(":") + 1).trim();
                    } else message = text;
                    Message m = new Message(manager.getLogin(), to, message);
                    int res = m.send(Utils.getURL() + "/add");
                    if (res != 200) { // 200 OK
                        System.out.println("HTTP error occurred: " + res);
                        return;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
