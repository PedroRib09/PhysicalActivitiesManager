package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.model.Error;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ErrorView implements CommandResult {
    LinkedList<String> list = new LinkedList<>();

    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
    }

    @Override
    public boolean unSuccessful() {
        return !list.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return list;
    }

    @Override
    public ArrayList fields() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    public void addToList(Error e) {
        list.add(e.toString());
    }
}
