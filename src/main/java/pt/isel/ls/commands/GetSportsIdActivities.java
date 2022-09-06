package pt.isel.ls.commands;

import pt.isel.ls.commandview.ErrorView;
import pt.isel.ls.commandview.GetSportsIdActivitiesView;
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


public class GetSportsIdActivities implements CommandHandler {
    private ActivityMapper activityMapper = new ActivityMapper();
    ErrorView errorView = new ErrorView();


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        GetSportsIdActivitiesView view = new GetSportsIdActivitiesView();
        try (Connection con = DriverManager.getConnection(System.getenv("DATABASE"))) {
            RouteResult r = commandRequest.getRouteResult();

            if (!ParameterCheck.isNumber(r.getKeyValue("sports"))) {
                errorView.addToList(new Error("Id is not an Integer!"));
            }

            ResultSet rs;
            String s;
            Parameters p = commandRequest.getParameters();
            if (p != null) {
                s = "select aid,a.sid,uid,duration,date,rid "
                        +
                        "FROM (SPORT s join ACTIVITY a on s.sid = a.sid) WHERE s.sid =?"
                        +
                        " OFFSET ? LIMIT ?";
            } else {
                s = "select aid,a.sid,uid,duration,date,rid "
                        +
                        "FROM (SPORT s join ACTIVITY a on s.sid = a.sid) WHERE s.sid =?";

            }

            try (PreparedStatement stmt = con.prepareStatement(s)) {
                stmt.setInt(1, Integer.parseInt((String) r.getKeyValue("sports")));
                if (p != null) {
                    stmt.setInt(2, Integer.parseInt((String) p.getMapValue("skip")));
                    stmt.setInt(3, Integer.parseInt((String) p.getMapValue("top")));
                }
                rs = stmt.executeQuery();
                view.map(rs, activityMapper);
            }
        } catch (NullPointerException e) {
            errorView.addToList(new Error("Connection Not Valid!"));
        }
        return view;
    }
}
