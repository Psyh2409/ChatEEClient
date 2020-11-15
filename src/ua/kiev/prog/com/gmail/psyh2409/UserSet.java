package ua.kiev.prog.com.gmail.psyh2409;

import java.util.HashSet;
import java.util.Set;

public class UserSet {
    private Set<User> userSet = new HashSet<>();

    public UserSet(Set<User> userSet) {
        this.userSet.addAll(userSet);
    }

    public Set<User> getUserSet() {
        return userSet;
    }
}
