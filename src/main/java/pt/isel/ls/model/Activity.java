package pt.isel.ls.model;

import java.sql.Timestamp;

public class Activity {

    int uid;
    int rid; //OPT
    String duration;
    String date;//OPT
    int aid;
    int sid;
    Timestamp timestamp;

    public int getUid() {
        return uid;
    }

    public int getRid() {
        return rid;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public int getAid() {
        return aid;
    }

    public int getSid() {
        return sid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Activity(int aid, String duration, String date, int uid, int rid, int sid, Timestamp timestamp) {
        this.uid = uid;
        this.rid = rid;
        this.aid = aid;
        this.sid = sid;
        this.duration = duration;
        this.date = date;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return " "
                + uid
                + ", "
                + rid
                + ", "
                + duration
                + ", "
                + date
                + ", "
                + aid
                + ", "
                + sid
                ;
    }
}
