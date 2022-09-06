package pt.isel.ls.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public interface DataMapper {
    LinkedList map(ResultSet resultSet, LinkedList list) throws SQLException;

    ArrayList getFields();

    void setFields(Object type, LinkedList<String> list) throws SQLException;
}
