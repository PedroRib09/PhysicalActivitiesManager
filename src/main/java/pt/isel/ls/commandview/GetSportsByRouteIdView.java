package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.mapper.SportMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class GetSportsByRouteIdView implements CommandResult {
    private final LinkedList<String> sports;

    public GetSportsByRouteIdView() {
        sports = new LinkedList<>();
    }

    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, sports);
    }

    @Override
    public boolean unSuccessful() {
        return sports.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return sports;
    }

    @Override
    public ArrayList fields() {
        SportMapper sportMapper = new SportMapper();
        return sportMapper.getFields();
    }

    @Override
    public String getType() {
        return "Sport";
    }
}
