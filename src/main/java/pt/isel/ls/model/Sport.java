package pt.isel.ls.model;


public class Sport {

    private String name;
    private String description;
    private int sid;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSid() {
        return sid;
    }

    public Sport(int sid, String name, String description) {
        this.name = name;
        this.description = description;
        this.sid = sid;
    }


    @Override
    public String toString() {
        return " "
                + name
                + ", "
                + description
                + ", "
                + sid;
    }
}
