package ua.kiev.prog.com.gmail.psyh2409;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.kiev.prog.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room {
    private String name;
    private String admin;
    private List<String> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Room(String name, String admin) {
        this.name = name;
        this.admin = admin;
    }

    public void showMates() {
        System.out.println("Room " + this.getName() + " has admin " + this.getAdmin() + " and next mates:");
        for (String s : users) {
            System.out.println(s);
        }
    }

    private String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Room fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, Room.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name) &&
                Objects.equals(admin, room.admin) &&
                Objects.equals(users, room.users) &&
                Objects.equals(messages, room.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, admin, users, messages);
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", admin=" + admin +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }
}
