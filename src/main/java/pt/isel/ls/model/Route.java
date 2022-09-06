package pt.isel.ls.model;

public class Route {

    private String start;
    private String end;
    private double distance;
    private int rid;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getDistance() {
        return distance;
    }

    public int getRid() {
        return rid;
    }

    public Route(int rid, String start, String end, double distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.rid = rid;
    }

    @Override
    public String toString() {
        return  " "
                + start
                + ", "
                + end
                + ", "
                + distance
                + ", "
                + rid
                ;
    }
}
