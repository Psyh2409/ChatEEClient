package ua.kiev.prog.com.gmail.psyh2409;

import com.google.gson.GsonBuilder;
import ua.kiev.prog.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Manager {
    private final Set<User> userSet;
    private final Scanner scanner;
    private String login = "";
    private String name = "";

    public Manager(Scanner scanner) {
        super();
        this.scanner = scanner;
        userSet = new HashSet<>();
        getUsers();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private void getUsers() {
        String brReadLine = null;
        try {
            HttpURLConnection hURLC = (HttpURLConnection) new URL("http://localhost:8080/getUsers").openConnection();
            hURLC.setRequestProperty("Content-Type", "application/json");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(hURLC.getInputStream()))) {
                brReadLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserSet users = new GsonBuilder().create().fromJson(brReadLine, UserSet.class);
        userSet.removeAll(users.getUserSet());
        userSet.addAll(users.getUserSet());
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void welcomeToHellp() {
        System.out.println("Welcome to chat HELLp!\nYou have next command:" +
                "\n/signup " +
                "\n/login " +
                "\n/users" +
                "\n/room" +
                "\n/leave" +
                "\n/invite" +
                "\n/exit" +
                "\n/help");
    }

    public void showUsers() {
        System.out.println("In Chat are next users:");
        for (User u : getSetOfUsers()) {
            System.out.println(u.getLogin() + " - " + (
                    u.getLogin().equals(login)
                            ?
                            u.isOnline()
                                    ? "offline" : "online"
                            :
                            u.isOnline()
                                    ? "online" : "offline"));
        }
    }

    private Set<User> getSetOfUsers() {
        getUsers();
        return userSet;
    }

    public void login() {
        while (true) {
            System.out.println("Please, enter your login that we know:");
            login = scanner.nextLine();
            if (catchCommand(login)) {
                boolean isOkLogin = false;
                User user = null;
                for (User u : getSetOfUsers()) {
                    if (u.getLogin().equals(login)) {
                        user = u;
                        isOkLogin = true;
                    }
                }
                if (isOkLogin) {
                    System.out.println("Well, if you really are " + login + ", " +
                            "enter " + login + "'s authentic password, or get out over here damn imposter!");
                    String password = scanner.nextLine();
                    if (catchCommand(password)) {
                        if (user.getPassword().equals(password)) {
                            System.out.println("Hello, " + login + "! Great that you are with us again!");
                            getUsers();
                            user.showMessages();
                            showUsers();
                            break;
                        } else {
                            System.out.println("Do not lie stinker, better enter your really login and password, or go to Hell!");
                        }
                    }
                } else {
                    System.out.println("We do not know you, " + login + ". Try signup firstly.");
                    signup();
                    break;
                }
            }
        }
    }

    public void signup() {
        System.out.println("Warning! Next that you enter will be your login:");
        login = scanner.nextLine();
        if (catchCommand(login)) {
            for (User u : getSetOfUsers()) {
                if (u.getLogin().equals(login)) {
                    System.out.println("We know user with login '" + login + "'. " +
                            "Better enter '/login' for sign in or '/signup' for enter new login.");
                    return;
                }
            }
            System.out.println("So, you are " + login + ". Well, nice to meet you, " + login + "!");
            while (true) {
                System.out.println("Please, " + login + ", enter your password below (remember it - you might repeat it later):");
                String password = scanner.nextLine();
                if (catchCommand(password)) {
                    System.out.println("How it is said, " + login + ", you have enter your password again:");
                    String pasCopy = scanner.nextLine();
                    if (catchCommand(pasCopy)) {
                        if (password.equals(pasCopy)) {
                            User user = new User(login, password);
                            try {
                                int resp = user.authorize(Utils.getURL() + "/auth");
                                if (resp == 403) {
                                    System.out.println("You are added to our community like login: '" + login + "'. ");
                                    user.update();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Wow, " + login + ", you did it! Welcome to Chat!");
                            getUsers();
                            showUsers();
                            break;
                        }
                    }
                }
                System.out.println("It wos wrong answer, " + login + "!\n" +
                        "You will train until it's done. It can be long, so do not spend time and letters.");
            }
        }
    }

    public boolean catchCommand(String text) {
        if (text.startsWith("/")) {
            switch (text) {
                case "/signup":
                    signup();
                    return false;
                case "/login":
                    login();
                    return false;
                case "/users":
                    showUsers();
                    return false;
                case "/help":
                    welcomeToHellp();
                    return false;
                case "/history":
                    for (User u : getSetOfUsers())
                        if (u.getLogin().equals(login))
                            u.showMessages();
                    return false;
                case "/room":
                    makeRoom();
                    return false;
                case "/mates":
                    for (User u : getSetOfUsers())
                        if (u.getLogin().equals(login))
                            u.getRoom().get(name).showMates();
                    return false;
            }
            this.login = "anonymous";
        }
        return true;
    }

    @Override
    public String toString() {
        return "Manager{" +
                ", userSet=" + getSetOfUsers() +
                ", scanner=" + scanner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return Objects.equals(getSetOfUsers(), manager.getSetOfUsers()) &&
                Objects.equals(scanner, manager.scanner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSetOfUsers(), scanner);
    }

    public void makeRoom() {
        System.out.println("Enter name of new room:");
        name = scanner.nextLine();
        if (catchCommand(name)) {
            User user = null;
            for (User u : getSetOfUsers()) {
                if (u.getLogin().equals(login)) user = u;
            }
            Room room = new Room(name, user.getLogin());
            showUsers();
            System.out.println("Enter all user's logins that you want to invite to room " + name + " one under one and empty line for finish adding:");
            while (true) {
                String uname = scanner.nextLine();
                if (uname.isEmpty()) break;
                if (catchCommand(uname)) {
                    for (User u : getSetOfUsers()) {
                        if (u.getLogin().equals(uname))
                            room.getUsers().add(u.getLogin());
                    }
                }
            }
            Map<String, Room> roomMap = user.getRoom();
            roomMap.put(name, room);
            user.update();
            getUsers();
            System.out.println("Room is created.");
        }
    }
}
