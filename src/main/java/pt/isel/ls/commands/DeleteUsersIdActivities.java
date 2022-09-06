package pt.isel.ls.commands;

import pt.isel.ls.commandview.DeleteUsersIdActivitiesView;
import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.ActivityMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DeleteUsersIdActivities implements CommandHandler {
    private ActivityMapper activityMapper = new ActivityMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        DeleteUsersIdActivitiesView view = new DeleteUsersIdActivitiesView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();
            Parameters p = commandRequest.getParameters();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            StringBuilder s = new StringBuilder("update activity set timestamp = ? where uid= ? and aid=?");
            ArrayList<Integer> list;
            String parameter = p.saveString();
            list = p.getActivitiesId(parameter);
            int i = 1;
            while (i < list.size()) {
                s.append(" or ");
                s.append(" aid=?");
                i++;
            }

            String h = s.toString();
            try (PreparedStatement stmt = con.prepareStatement(h,PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setTimestamp(1, timestamp);
                stmt.setInt(2, Integer.parseInt((String) r.getKeyValue("users")));
                i = 0;
                int j = 3;
                while (i < list.size()) {
                    stmt.setInt(j, list.get(i));
                    i++;
                    j++;
                }

                stmt.execute();
                ResultSet rs;
                rs = stmt.getGeneratedKeys();
                view.map(rs, activityMapper);
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}

