package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;


public class GetUsersView implements CommandResult {
    private final LinkedList<String> users;

    public GetUsersView() {
        users = new LinkedList<>();
    }

    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, users);
    }

    @Override
    public boolean unSuccessful() {
        return users.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return users;
    }

    @Override
    public ArrayList fields() {
        UserMapper userMapper = new UserMapper();
        return userMapper.getFields();
    }

    @Override
    public String getType() {
        return "User";
    }


}