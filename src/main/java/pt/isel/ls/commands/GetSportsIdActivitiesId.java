package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetSportsIdActivitiesIdView;
import pt.isel.ls.model.Error;
import pt.isel.ls.cmdhandling.CommandHandler;
import pt.isel.ls.cmdhandling.CommandRequest;
import pt.isel.ls.cmdhandling.CommandResult;
import pt.isel.ls.cmdhandling.RouteResult;
import pt.isel.ls.mapper.ActivityMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSportsIdActivitiesId implements CommandHandler {
    ErrorView errorView = new ErrorView();

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetSportsIdActivitiesIdView view = new GetSportsIdActivitiesIdView();

        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {

            RouteResult r = commandRequest.getRouteResult();

            if (!ParameterCheck.isNumber(r.getKeyValue("sports"))
                    || !ParameterCheck.isNumber(r.getKeyValue("activities"))) {
                errorView.addToList(new Error("Id is not an Integer!"));
            }

            ResultSet rs;
            String s = "select * FROM (SPORT s join ACTIVITY a on s.sid = a.sid) WHERE s.sid =? AND a.aid=?";
            try (PreparedStatement stmt = con.prepareStatement(s)) {
                stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("sports")));
                stmt.setInt(2, Integer.parseInt((String) r.getKeyValue("activities")));
                rs = stmt.executeQuery();
                ActivityMapper activityMapper = new ActivityMapper();
                view.map(rs, activityMapper);
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
