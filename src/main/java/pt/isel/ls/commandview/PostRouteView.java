package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.mapper.RouteMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class PostRouteView implements CommandResult {
    private final LinkedList<String> route;

    public PostRouteView() {
        route = new LinkedList<>();
    }


    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, route);
    }

    @Override
    public boolean unSuccessful() {
        return route.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return route;
    }

    @Override
    public ArrayList fields() {
        RouteMapper routeMapper = new RouteMapper();
        return routeMapper.getFields();
    }

    @Override
    public String getType() {
        return "Route";
    }
}


