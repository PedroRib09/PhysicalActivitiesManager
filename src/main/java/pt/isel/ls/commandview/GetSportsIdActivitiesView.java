package pt.isel.ls.commandview;

import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.mapper.ActivityMapper;
import pt.isel.ls.mapper.DataMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;


public class GetSportsIdActivitiesView implements CommandResult {
    private final LinkedList<String> activity;

    public GetSportsIdActivitiesView() {
        activity = new LinkedList<>();
    }


    @Override
    public void map(ResultSet resultSet, DataMapper mapper) throws SQLException {
        mapper.map(resultSet, activity);
    }

    @Override
    public boolean unSuccessful() {
        return activity.isEmpty();
    }

    @Override
    public LinkedList<String> getList() {
        return activity;
    }

    @Override
    public ArrayList fields() {
        ActivityMapper activityMapper = new ActivityMapper();
        return activityMapper.getFields();
    }

    @Override
    public String getType() {
        return "Activity";
    }
}