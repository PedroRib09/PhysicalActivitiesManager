package pt.isel.ls.mapper;

import pt.isel.ls.model.Sport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SportMapper implements DataMapper {
    @Override
    public LinkedList map(ResultSet rs, LinkedList list) throws SQLException {
        while (rs.next()) {
            Sport sport = new Sport(rs.getInt("sid"), rs.getString("name"),
                    rs.getString("description"));
            setFields(sport, list);
        }
        return list;
    }

    @Override
    public ArrayList<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("sid");
        list.add("name");
        list.add("description");
        return list;
    }

    @Override
    public void setFields(Object obj, LinkedList<String> list) {
        Sport sport = (Sport) obj;
        list.add(String.valueOf(sport.getSid()));
        list.add(sport.getName());
        list.add(sport.getDescription());
    }
}
