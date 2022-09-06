package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.DataMapper;
import pt.isel.ls.mapper.UserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class GetUserByIdView implements CommandResult {

    private final LinkedList<String> user;

    public GetUserByIdView() {
        user = new LinkedList<>();
    }


    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, user);
    }

    @Override
    public boolean unSuccessful() {
        return user.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return user;
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
