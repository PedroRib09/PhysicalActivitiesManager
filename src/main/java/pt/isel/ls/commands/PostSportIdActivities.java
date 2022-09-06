package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.PostSportIdActivitiesView;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.Parameters;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.ActivityMapper;
import pt.isel.ls.model.Error;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostSportIdActivities implements CommandHandler {
    private ActivityMapper activityMapper = new ActivityMapper();
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        PostSportIdActivitiesView view = new PostSportIdActivitiesView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            Parameters p = commandRequest.getParameters();

            RouteResult routeResult = commandRequest.getRouteResult();
            if (!(ParameterCheck.checkDuration(p.getMapValue("duration")))) {
                errorView.addToList(new Error("Duration format not valid"));
            }
            if (!(ParameterCheck.checkDate(p.getMapValue("date")))) {
                errorView.addToList(new Error("Date not valid"));
            }
            if (errorView.getList().isEmpty()) {

                String s = "INSERT INTO activity (duration, date, uid, " + (p.getMapValue("rid") == null ? "" :
                        "rid" + ",") + " sid)"
                        +
                        " VALUES (?,?,? " + (p.getMapValue("rid") == null ? "" : ","
                        + "?") + ", ? )";

                try (PreparedStatement stmt = con.prepareStatement(s, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, (String) p.getMapValue("duration"));
                    stmt.setString(2, (String) p.getMapValue("date"));
                    stmt.setInt(3, Integer.parseInt((String) p.getMapValue("uid")));
                    if (p.getMapValue("rid") == null) {
                        stmt.setInt(4, Integer.parseInt((String) routeResult.getKeyValue("sports")));
                    } else {
                        stmt.setInt(4, Integer.parseInt((String) p.getMapValue("rid")));
                        stmt.setInt(5, Integer.parseInt((String) routeResult.getKeyValue("sports")));
                    }
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    view.map(rs, activityMapper);
                }
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        if (!errorView.getList().isEmpty()) {
            return errorView;
        }
        return view;
    }
}
