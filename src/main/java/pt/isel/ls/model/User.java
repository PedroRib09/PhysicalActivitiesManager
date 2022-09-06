package pt.isel.ls.model;

public class User {
    private String name;
    private String email;
    private int uid;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getUid() {
        return uid;
    }

    public User(int uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return  " "
                + name
                + ", "
                + email
                + ", "
                + uid;
    }
}