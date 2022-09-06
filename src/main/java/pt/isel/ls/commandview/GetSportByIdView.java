package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.mapper.SportMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class GetSportByIdView implements CommandResult {
    private final LinkedList<String> sports;

    public GetSportByIdView() {
        sports = new LinkedList<>();
    }


    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, sports);
    }

    @Override
    public boolean unSuccessful() {
        return sports.size() != 1;
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
