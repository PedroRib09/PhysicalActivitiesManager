package pt.isel.ls.mapper;

import pt.isel.ls.model.Activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ActivityMapper implements DataMapper {
    @Override
    public LinkedList map(ResultSet rs, LinkedList list) throws SQLException {
        while (rs.next()) {
            Activity activity = new Activity(rs.getInt("aid"), rs.getString("duration"),
                    rs.getString("date"), rs.getInt("uid"), rs.getInt("rid"),
                    rs.getInt("sid"), null);
            setFields(activity, list);
        }
        return list;
    }

    @Override
    public ArrayList<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("aid");
        list.add("duration");
        list.add("date");
        list.add("uid");
        list.add("rid");
        list.add("sid");
        return list;
    }

    @Override
    public void setFields(Object obj, LinkedList<String> list) {
        Activity activity = (Activity) obj;
        list.add(String.valueOf(activity.getAid()));
        list.add(activity.getDuration());
        list.add(activity.getDate());
        list.add(String.valueOf(activity.getUid()));
        list.add(String.valueOf(activity.getRid()));
        list.add(String.valueOf(activity.getSid()));
    }
}
