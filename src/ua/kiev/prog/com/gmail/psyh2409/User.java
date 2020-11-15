package ua.kiev.prog.com.gmail.psyh2409;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.kiev.prog.Message;
import ua.kiev.prog.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class User {
    private String login;
    private String password;
    private boolean isOnline;
    private final List<Message> mesList = new ArrayList<>();
    private final Map<String, Room> rooms = new HashMap<>();

    public User() {
        super();
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Map<String, Room> getRoom() {
        return rooms;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Message> getMesList() {
        return mesList;
    }

    public void showMessages() {
        System.out.println("These are your privat message:");
        for (Message m : mesList) {
            System.out.println(m);
        }
    }

    public void update() {
        OutputStream os = null;
        try {
            URL url = new URL(Utils.getURL() + "/update");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();

            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            os = huc.getOutputStream();
            String string = toJSON();
            os.write(string.getBytes(StandardCharsets.UTF_8));
            int code = huc.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int authorize(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        try {
            String json = toJSON();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            return conn.getResponseCode();
        } finally {
            os.close();
        }
    }

    protected String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static User fromJSON(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, User.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", isOnline=" + isOnline +
                ", mesList=" + mesList +
                ", rooms=" + rooms +
                '}';
    }
}
