package pt.isel.ls.mapper;

import pt.isel.ls.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class UserMapper implements DataMapper {

    @Override
    public LinkedList map(ResultSet rs, LinkedList list) throws SQLException {
        while (rs.next()) {
            User user = new User(rs.getInt("uid"), rs.getString("name"),
                    rs.getString("email"));
            setFields(user, list);
        }

        return list;
    }

    @Override
    public ArrayList<String> getFields() {
        ArrayList<String> list = new ArrayList<>();
        list.add("uid");
        list.add("name");
        list.add("email");
        return list;
    }

    @Override
    public void setFields(Object obj, LinkedList<String> list) {
        User user = (User) obj;
        list.add(String.valueOf(user.getUid()));
        list.add(user.getName());
        list.add(user.getEmail());
    }
}

