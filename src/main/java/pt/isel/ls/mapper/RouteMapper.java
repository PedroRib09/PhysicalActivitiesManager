package pt.isel.ls.mapper;

import pt.isel.ls.model.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class RouteMapper implements DataMapper {
    @Override
    public LinkedList map(ResultSet rs, LinkedList list) throws SQLException {
        while (rs.next()) {
            Route route = new Route(rs.getInt("rid"), rs.getString("startloc"),
                    rs.getString("endloc"), rs.getDouble("distance"));
            setFields(route, list);
        }
        return list;
    }

    @Override
    public ArrayList<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("rid");
        list.add("start");
        list.add("end");
        list.add("distance");
        return list;
    }

    @Override
    public void setFields(Object obj, LinkedList<String> list) {
        Route route = (Route) obj;
        list.add(String.valueOf(route.getRid()));
        list.add(route.getStart());
        list.add(route.getEnd());
        list.add(String.valueOf(route.getDistance()));
    }
}
