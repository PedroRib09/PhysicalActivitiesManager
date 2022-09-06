package pt.isel.ls.cmdhandling;

import pt.isel.ls.mapper.DataMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public interface CommandResult {

    void map(ResultSet resultSet, DataMapper mapper) throws SQLException;

    boolean unSuccessful();

    LinkedList<String> getList();

    ArrayList fields();

    String getType();

}
